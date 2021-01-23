from collections import defaultdict

import requests
from bs4 import BeautifulSoup

INTERFACE = {
    'Markup': [
        'InlineKeyboardMarkup',
        'ReplyKeyboardMarkup',
        'ReplyKeyboardRemove',
        'ForceReply'
    ],
    'InputMedia': [
        'InputMediaPhoto',
        'InputMediaVideo',
        'InputMediaDocument',
        'InputMediaVideo'
    ]
}

INVERTED_INTERFACES = {
    subtype: type_name
    for type_name, subtypes in INTERFACE.items()
    for subtype in subtypes
}

ENUMS = {
    'ChatType': {
        'PRIVATE': 'private',
        'GROUP': 'group',
        'SUPERGROUP': 'supergroup',
        'CHANNEL': 'channel',
    },
    'ParseMode': {
        'MARKDOWN_V2': 'MarkdownV2',
        'MARKDOWN': 'Markdown',
        'HTML': 'HTML'
    },
    'ChatAction': {
        'TYPING': 'typing',
        'UPLOAD_PHOTO': 'upload_photo',
        'UPLOAD_AUDIO': 'upload_audio',
        'UPLOAD_DOCUMENT': 'upload_document',
        'FIND_LOCATION': 'find_location',
        'RECORD_VIDEO_NOTE': 'record_video_note',
        'UPLOAD_VIDEO_NOTE': 'upload_video_note',
    }
}

CUSTOM_TYPES = {
    'Chat': {
        'type': 'ChatType'
    },
    'SendChatActionRequest': {
        'action': 'ChatAction'
    },
    '__GLOBAL__': {
        'parseMode': 'ParseMode'
    },
}


def to_camel_case(name):
    items = name.split('_')

    return ''.join([items[0]] + [item[0].capitalize() + item[1:] for item in items[1:]])


def download_api_docs():
    return requests.get('https://core.telegram.org/bots/api').text


def generate_model(html):
    soup = BeautifulSoup(html, 'html.parser')

    content = soup.find(id='dev_page_content')
    items = list(content.children)

    current_headline = None
    sections = defaultdict(list)
    for item in items:
        if item.name == 'h3':
            current_headline = item.text
        else:
            sections[current_headline].append(item)

    result = []

    types = [parse_types(section) for section in sections.values()]
    types = [x for y in types for x in y]

    result += ["""@file:Suppress("unused")

package ru.darkkeks.telegram.core.api

import com.fasterxml.jackson.annotation.JsonValue"""]
    result += [generate_type(type_info) for type_info in types]
    result += generate_enums()
    result += generate_interfaces(types)

    return '\n\n\n'.join(result) + '\n'


def generate_enums():
    result = []
    for name, items in ENUMS.items():
        current = f'enum class {name}(@JsonValue val jsonName: String) {{\n'
        enum_values = [f'    {key}("{value}")' for key, value in items.items()]
        current += ',\n'.join(enum_values) + ';\n'
        current += """
    override fun toString() = jsonName
}"""
        result.append(current)
    return result


def generate_interfaces(existing_types):
    type_names = [type_info['name'] for type_info in existing_types]
    return [
        f"interface {name}" for name in INTERFACE.keys() if name not in type_names
    ]


def generate_type(type_info):
    result = ''
    result += '/**\n'
    result += format_doc(type_info['doc'])
    result += ' */\n'

    if type_info['name'] in INTERFACE:
        result += 'interface '
    elif len(type_info['fields']) > 0:
        result += 'data class '
    else:
        result += 'class '

    result += type_info['name']

    if len(type_info['fields']) > 0:
        result += '('

        for idx, field in enumerate(type_info['fields']):
            field_name = to_camel_case(field['name'])
            field_type = process_type(field['type'], field_name, type_info['name'], field['desc'])

            if idx > 0:
                result += ','
            result += '\n\n'
            result += f'    /**\n'
            result += format_doc(field['desc'], 4)
            result += f'     */\n'
            result += f"    val {field_name}: {field_type}"
            if 'optional' in field:
                result += '? = null'

        result += '\n)'

    if type_info['name'] in INVERTED_INTERFACES:
        subtype = INVERTED_INTERFACES[type_info['name']]
        result += " : " + subtype

    return result


def format_doc(doc, indent=0):
    length = 116 - indent

    result = []

    sentences = doc.split('\n')
    for sentence in sentences:
        words = sentence.split(' ')
        lines = []

        current_line = 228
        for word in words:
            if current_line + 1 + len(word) > length:
                lines.append('')
                current_line = 0

            current_line += 1 + len(word)
            lines[-1] += ' ' + word

        result.append(''.join([indent * ' ' + ' * ' + line + '\n' for line in lines]))

    return (indent * ' ' + ' *\n').join(result)


def process_type(typename, field_name, type_name, desc):
    if type_name in CUSTOM_TYPES and field_name in CUSTOM_TYPES[type_name]:
        return CUSTOM_TYPES[type_name][field_name]

    if field_name in CUSTOM_TYPES['__GLOBAL__']:
        return CUSTOM_TYPES['__GLOBAL__'][field_name]

    while typename.startswith('Array of '):
        typename = f"List<{process_type(typename.replace('Array of ', '', 1), field_name, type_name, desc)}>"

    if ' or ' in typename:
        if 'Markup' in typename:
            typename = 'Markup'
        else:
            typename = typename.split(' or ')[0]

    if ', ' in typename:
        if 'InputMedia' in typename:
            typename = 'InputMedia'
        else:
            raise Exception(f'Unknown type {typename}')

    if typename == 'Integer':
        if '64 bit' in desc or field_name.lower().endswith('chatid'):
            typename = 'Long'
        else:
            typename = 'Int'

    if typename == 'True':
        typename = 'Boolean'

    if typename == 'Float number':
        typename = 'Double'

    return typename


def parse_types(items):
    types = defaultdict(list)

    current_type = None
    for item in items:
        if item.name == 'h4':
            current_type = item.text
        else:
            types[current_type].append(item)

    result = []
    for type, elements in types.items():
        if type is None or ' ' in type:
            print(f'skipping {type}')
            continue

        is_method = type[0].islower()
        if is_method:
            type = type[0].upper() + type[1:] + 'Request'

        type_info = {
            'name': type,
            'doc': '',
            'fields': []
        }

        for elem in elements:
            if elem.name == 'table':
                for row in elem.find('tbody').find_all('tr'):
                    field = {}
                    for idx, col in enumerate(row.find_all('td')):
                        if idx == 0:
                            field['name'] = col.text
                        elif idx == 1:
                            field['type'] = col.text
                        elif is_method and idx == 2:
                            if 'Optional' in col.text:
                                field['optional'] = True
                        elif (not is_method and idx == 2) or (is_method and idx == 3):
                            desc = col.text
                            if desc.startswith('Optional. '):
                                field['optional'] = True
                                desc = desc.replace('Optional. ', '')
                            field['desc'] = desc
                    type_info['fields'].append(field)
            elif elem.name == 'p':
                if type_info['doc'] == '':
                    type_info['doc'] = elem.text
                else:
                    type_info['doc'] += '\n' + elem.text

        result.append(type_info)
    return result


target_path = '../kotlin/ru/darkkeks/telegram/core/api/model.kt'

if __name__ == '__main__':
    doc = download_api_docs()

    with open(target_path, 'w') as f:
        f.write(generate_model(doc))
