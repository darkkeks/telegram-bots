from telethon import TelegramClient, events, sync
import re
from time import sleep

# These example values won't work. You must get your own api_id and
# api_hash from https://my.telegram.org, under API Development.
api_id = -1
api_hash = ''

client = TelegramClient('session_name', api_id, api_hash)
client.start()

dialogs = client.get_dialogs(limit = 100)

lectures = None
for dialog in dialogs:
    if dialog.name == 'Лекции':
        lectures = dialog
    
# print(lectures)

messages = client.get_messages(lectures, limit=1000)

for message in messages:
    if message.message is None:
        continue

    text = message.message

    if '#' not in text:
        continue

    if '_Л' not in text and '#Г' not in text:
        lines = text.splitlines()

        tags = []

        name = re.search(r'#(\w+)', text)[1]

        group = re.search(r'19\d{1,2}', text)
        if group is not None:
            tags += ['#Г' + group[0]]
            tags += ['#' + name + '_' + group[0]]

        if 'екция' in text or 'ect' in text:
            tags += ['#' + name + '_Л']

        lines[-1] += ' ' + ' '.join(tags)
        new_text = '\n'.join(lines)

        if not tags:
            continue

        print(new_text)
        client.edit_message(lectures, message, link_preview=False, text=new_text)
        print('Sleeping')
        sleep(3)
        print('Done')
