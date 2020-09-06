package ru.darkkeks.telegram.core.api

import com.fasterxml.jackson.annotation.JsonValue


/**
 *  This object represents an incoming update.At most one of the optional parameters can be present in any given
 *  update.
 */
data class Update(

        /**
         *  The update's unique identifier. Update identifiers start from a certain positive number and increase
         *  sequentially. This ID becomes especially handy if you're using Webhooks, since it allows you to ignore
         *  repeated updates or to restore the correct update sequence, should they get out of order. If there are no
         *  new updates for at least a week, then identifier of the next update will be chosen randomly instead of
         *  sequentially.
         */
        val updateId: Int,

        /**
         *  New incoming message of any kind — text, photo, sticker, etc.
         */
        val message: Message? = null,

        /**
         *  New version of a message that is known to the bot and was edited
         */
        val editedMessage: Message? = null,

        /**
         *  New incoming channel post of any kind — text, photo, sticker, etc.
         */
        val channelPost: Message? = null,

        /**
         *  New version of a channel post that is known to the bot and was edited
         */
        val editedChannelPost: Message? = null,

        /**
         *  New incoming inline query
         */
        val inlineQuery: InlineQuery? = null,

        /**
         *  The result of an inline query that was chosen by a user and sent to their chat partner. Please see our
         *  documentation on the feedback collecting for details on how to enable these updates for your bot.
         */
        val chosenInlineResult: ChosenInlineResult? = null,

        /**
         *  New incoming callback query
         */
        val callbackQuery: CallbackQuery? = null,

        /**
         *  New incoming shipping query. Only for invoices with flexible price
         */
        val shippingQuery: ShippingQuery? = null,

        /**
         *  New incoming pre-checkout query. Contains full information about checkout
         */
        val preCheckoutQuery: PreCheckoutQuery? = null,

        /**
         *  New poll state. Bots receive only updates about stopped polls and polls, which are sent by the bot
         */
        val poll: Poll? = null,

        /**
         *  A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by
         *  the bot itself.
         */
        val pollAnswer: PollAnswer? = null
)


/**
 *  Contains information about the current status of a webhook.
 */
data class WebhookInfo(

        /**
         *  Webhook URL, may be empty if webhook is not set up
         */
        val url: String,

        /**
         *  True, if a custom certificate was provided for webhook certificate checks
         */
        val hasCustomCertificate: Boolean,

        /**
         *  Number of updates awaiting delivery
         */
        val pendingUpdateCount: Int,

        /**
         *  Unix time for the most recent error that happened when trying to deliver an update via webhook
         */
        val lastErrorDate: Int? = null,

        /**
         *  Error message in human-readable format for the most recent error that happened when trying to deliver an
         *  update via webhook
         */
        val lastErrorMessage: String? = null,

        /**
         *  Maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery
         */
        val maxConnections: Int? = null,

        /**
         *  A list of update types the bot is subscribed to. Defaults to all update types
         */
        val allowedUpdates: List<String>? = null
)


/**
 *  This object represents a Telegram user or bot.
 */
data class User(

        /**
         *  Unique identifier for this user or bot
         */
        val id: Int,

        /**
         *  True, if this user is a bot
         */
        val isBot: Boolean,

        /**
         *  User's or bot's first name
         */
        val firstName: String,

        /**
         *  User's or bot's last name
         */
        val lastName: String? = null,

        /**
         *  User's or bot's username
         */
        val username: String? = null,

        /**
         *  IETF language tag of the user's language
         */
        val languageCode: String? = null,

        /**
         *  True, if the bot can be invited to groups. Returned only in getMe.
         */
        val canJoinGroups: Boolean? = null,

        /**
         *  True, if privacy mode is disabled for the bot. Returned only in getMe.
         */
        val canReadAllGroupMessages: Boolean? = null,

        /**
         *  True, if the bot supports inline queries. Returned only in getMe.
         */
        val supportsInlineQueries: Boolean? = null
)


/**
 *  This object represents a chat.
 */
data class Chat(

        /**
         *  Unique identifier for this chat. This number may be greater than 32 bits and some programming languages may
         *  have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit
         *  integer or double-precision float type are safe for storing this identifier.
         */
        val id: Long,

        /**
         *  Type of chat, can be either “private”, “group”, “supergroup” or “channel”
         */
        val type: ChatType,

        /**
         *  Title, for supergroups, channels and group chats
         */
        val title: String? = null,

        /**
         *  Username, for private chats, supergroups and channels if available
         */
        val username: String? = null,

        /**
         *  First name of the other party in a private chat
         */
        val firstName: String? = null,

        /**
         *  Last name of the other party in a private chat
         */
        val lastName: String? = null,

        /**
         *  Chat photo. Returned only in getChat.
         */
        val photo: ChatPhoto? = null,

        /**
         *  Description, for groups, supergroups and channel chats. Returned only in getChat.
         */
        val description: String? = null,

        /**
         *  Chat invite link, for groups, supergroups and channel chats. Each administrator in a chat generates their
         *  own invite links, so the bot must first generate the link using exportChatInviteLink. Returned only in
         *  getChat.
         */
        val inviteLink: String? = null,

        /**
         *  Pinned message, for groups, supergroups and channels. Returned only in getChat.
         */
        val pinnedMessage: Message? = null,

        /**
         *  Default chat member permissions, for groups and supergroups. Returned only in getChat.
         */
        val permissions: ChatPermissions? = null,

        /**
         *  For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user.
         *  Returned only in getChat.
         */
        val slowModeDelay: Int? = null,

        /**
         *  For supergroups, name of group sticker set. Returned only in getChat.
         */
        val stickerSetName: String? = null,

        /**
         *  True, if the bot can change the group sticker set. Returned only in getChat.
         */
        val canSetStickerSet: Boolean? = null
)


/**
 *  This object represents a message.
 */
data class Message(

        /**
         *  Unique message identifier inside this chat
         */
        val messageId: Int,

        /**
         *  Sender, empty for messages sent to channels
         */
        val from: User? = null,

        /**
         *  Date the message was sent in Unix time
         */
        val date: Int,

        /**
         *  Conversation the message belongs to
         */
        val chat: Chat,

        /**
         *  For forwarded messages, sender of the original message
         */
        val forwardFrom: User? = null,

        /**
         *  For messages forwarded from channels, information about the original channel
         */
        val forwardFromChat: Chat? = null,

        /**
         *  For messages forwarded from channels, identifier of the original message in the channel
         */
        val forwardFromMessageId: Int? = null,

        /**
         *  For messages forwarded from channels, signature of the post author if present
         */
        val forwardSignature: String? = null,

        /**
         *  Sender's name for messages forwarded from users who disallow adding a link to their account in forwarded
         *  messages
         */
        val forwardSenderName: String? = null,

        /**
         *  For forwarded messages, date the original message was sent in Unix time
         */
        val forwardDate: Int? = null,

        /**
         *  For replies, the original message. Note that the Message object in this field will not contain further
         *  reply_to_message fields even if it itself is a reply.
         */
        val replyToMessage: Message? = null,

        /**
         *  Bot through which the message was sent
         */
        val viaBot: User? = null,

        /**
         *  Date the message was last edited in Unix time
         */
        val editDate: Int? = null,

        /**
         *  The unique identifier of a media message group this message belongs to
         */
        val mediaGroupId: String? = null,

        /**
         *  Signature of the post author for messages in channels
         */
        val authorSignature: String? = null,

        /**
         *  For text messages, the actual UTF-8 text of the message, 0-4096 characters
         */
        val text: String? = null,

        /**
         *  For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
         */
        val entities: List<MessageEntity>? = null,

        /**
         *  Message is an animation, information about the animation. For backward compatibility, when this field is
         *  set, the document field will also be set
         */
        val animation: Animation? = null,

        /**
         *  Message is an audio file, information about the file
         */
        val audio: Audio? = null,

        /**
         *  Message is a general file, information about the file
         */
        val document: Document? = null,

        /**
         *  Message is a photo, available sizes of the photo
         */
        val photo: List<PhotoSize>? = null,

        /**
         *  Message is a sticker, information about the sticker
         */
        val sticker: Sticker? = null,

        /**
         *  Message is a video, information about the video
         */
        val video: Video? = null,

        /**
         *  Message is a video note, information about the video message
         */
        val videoNote: VideoNote? = null,

        /**
         *  Message is a voice message, information about the file
         */
        val voice: Voice? = null,

        /**
         *  Caption for the animation, audio, document, photo, video or voice, 0-1024 characters
         */
        val caption: String? = null,

        /**
         *  For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the
         *  caption
         */
        val captionEntities: List<MessageEntity>? = null,

        /**
         *  Message is a shared contact, information about the contact
         */
        val contact: Contact? = null,

        /**
         *  Message is a dice with random value from 1 to 6
         */
        val dice: Dice? = null,

        /**
         *  Message is a game, information about the game. More about games »
         */
        val game: Game? = null,

        /**
         *  Message is a native poll, information about the poll
         */
        val poll: Poll? = null,

        /**
         *  Message is a venue, information about the venue. For backward compatibility, when this field is set, the
         *  location field will also be set
         */
        val venue: Venue? = null,

        /**
         *  Message is a shared location, information about the location
         */
        val location: Location? = null,

        /**
         *  New members that were added to the group or supergroup and information about them (the bot itself may be
         *  one of these members)
         */
        val newChatMembers: List<User>? = null,

        /**
         *  A member was removed from the group, information about them (this member may be the bot itself)
         */
        val leftChatMember: User? = null,

        /**
         *  A chat title was changed to this value
         */
        val newChatTitle: String? = null,

        /**
         *  A chat photo was change to this value
         */
        val newChatPhoto: List<PhotoSize>? = null,

        /**
         *  Service message: the chat photo was deleted
         */
        val deleteChatPhoto: Boolean? = null,

        /**
         *  Service message: the group has been created
         */
        val groupChatCreated: Boolean? = null,

        /**
         *  Service message: the supergroup has been created. This field can't be received in a message coming through
         *  updates, because bot can't be a member of a supergroup when it is created. It can only be found in
         *  reply_to_message if someone replies to a very first message in a directly created supergroup.
         */
        val supergroupChatCreated: Boolean? = null,

        /**
         *  Service message: the channel has been created. This field can't be received in a message coming through
         *  updates, because bot can't be a member of a channel when it is created. It can only be found in
         *  reply_to_message if someone replies to a very first message in a channel.
         */
        val channelChatCreated: Boolean? = null,

        /**
         *  The group has been migrated to a supergroup with the specified identifier. This number may be greater than
         *  32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is
         *  smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this
         *  identifier.
         */
        val migrateToChatId: Long? = null,

        /**
         *  The supergroup has been migrated from a group with the specified identifier. This number may be greater
         *  than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it
         *  is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing
         *  this identifier.
         */
        val migrateFromChatId: Long? = null,

        /**
         *  Specified message was pinned. Note that the Message object in this field will not contain further
         *  reply_to_message fields even if it is itself a reply.
         */
        val pinnedMessage: Message? = null,

        /**
         *  Message is an invoice for a payment, information about the invoice. More about payments »
         */
        val invoice: Invoice? = null,

        /**
         *  Message is a service message about a successful payment, information about the payment. More about payments
         *  »
         */
        val successfulPayment: SuccessfulPayment? = null,

        /**
         *  The domain name of the website on which the user has logged in. More about Telegram Login »
         */
        val connectedWebsite: String? = null,

        /**
         *  Telegram Passport data
         */
        val passportData: PassportData? = null,

        /**
         *  Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons.
         */
        val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 */
data class MessageEntity(

        /**
         *  Type of the entity. Can be “mention” (@username), “hashtag” (#hashtag), “cashtag” ($USD), “bot_command”
         *  (/start@jobs_bot), “url” (https://telegram.org), “email” (do-not-reply@telegram.org), “phone_number”
         *  (+1-212-555-0123), “bold” (bold text), “italic” (italic text), “underline” (underlined text),
         *  “strikethrough” (strikethrough text), “code” (monowidth string), “pre” (monowidth block), “text_link” (for
         *  clickable text URLs), “text_mention” (for users without usernames)
         */
        val type: String,

        /**
         *  Offset in UTF-16 code units to the start of the entity
         */
        val offset: Int,

        /**
         *  Length of the entity in UTF-16 code units
         */
        val length: Int,

        /**
         *  For “text_link” only, url that will be opened after user taps on the text
         */
        val url: String? = null,

        /**
         *  For “text_mention” only, the mentioned user
         */
        val user: User? = null,

        /**
         *  For “pre” only, the programming language of the entity text
         */
        val language: String? = null
)


/**
 *  This object represents one size of a photo or a file / sticker thumbnail.
 */
data class PhotoSize(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  Photo width
         */
        val width: Int,

        /**
         *  Photo height
         */
        val height: Int,

        /**
         *  File size
         */
        val fileSize: Int? = null
)


/**
 *  This object represents an animation file (GIF or H.264/MPEG-4 AVC video without sound).
 */
data class Animation(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  Video width as defined by sender
         */
        val width: Int,

        /**
         *  Video height as defined by sender
         */
        val height: Int,

        /**
         *  Duration of the video in seconds as defined by sender
         */
        val duration: Int,

        /**
         *  Animation thumbnail as defined by sender
         */
        val thumb: PhotoSize? = null,

        /**
         *  Original animation filename as defined by sender
         */
        val fileName: String? = null,

        /**
         *  MIME type of the file as defined by sender
         */
        val mimeType: String? = null,

        /**
         *  File size
         */
        val fileSize: Int? = null
)


/**
 *  This object represents an audio file to be treated as music by the Telegram clients.
 */
data class Audio(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  Duration of the audio in seconds as defined by sender
         */
        val duration: Int,

        /**
         *  Performer of the audio as defined by sender or by audio tags
         */
        val performer: String? = null,

        /**
         *  Title of the audio as defined by sender or by audio tags
         */
        val title: String? = null,

        /**
         *  MIME type of the file as defined by sender
         */
        val mimeType: String? = null,

        /**
         *  File size
         */
        val fileSize: Int? = null,

        /**
         *  Thumbnail of the album cover to which the music file belongs
         */
        val thumb: PhotoSize? = null
)


/**
 *  This object represents a general file (as opposed to photos, voice messages and audio files).
 */
data class Document(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  Document thumbnail as defined by sender
         */
        val thumb: PhotoSize? = null,

        /**
         *  Original filename as defined by sender
         */
        val fileName: String? = null,

        /**
         *  MIME type of the file as defined by sender
         */
        val mimeType: String? = null,

        /**
         *  File size
         */
        val fileSize: Int? = null
)


/**
 *  This object represents a video file.
 */
data class Video(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  Video width as defined by sender
         */
        val width: Int,

        /**
         *  Video height as defined by sender
         */
        val height: Int,

        /**
         *  Duration of the video in seconds as defined by sender
         */
        val duration: Int,

        /**
         *  Video thumbnail
         */
        val thumb: PhotoSize? = null,

        /**
         *  Mime type of a file as defined by sender
         */
        val mimeType: String? = null,

        /**
         *  File size
         */
        val fileSize: Int? = null
)


/**
 *  This object represents a video message (available in Telegram apps as of v.4.0).
 */
data class VideoNote(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  Video width and height (diameter of the video message) as defined by sender
         */
        val length: Int,

        /**
         *  Duration of the video in seconds as defined by sender
         */
        val duration: Int,

        /**
         *  Video thumbnail
         */
        val thumb: PhotoSize? = null,

        /**
         *  File size
         */
        val fileSize: Int? = null
)


/**
 *  This object represents a voice note.
 */
data class Voice(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  Duration of the audio in seconds as defined by sender
         */
        val duration: Int,

        /**
         *  MIME type of the file as defined by sender
         */
        val mimeType: String? = null,

        /**
         *  File size
         */
        val fileSize: Int? = null
)


/**
 *  This object represents a phone contact.
 */
data class Contact(

        /**
         *  Contact's phone number
         */
        val phoneNumber: String,

        /**
         *  Contact's first name
         */
        val firstName: String,

        /**
         *  Contact's last name
         */
        val lastName: String? = null,

        /**
         *  Contact's user identifier in Telegram
         */
        val userId: Int? = null,

        /**
         *  Additional data about the contact in the form of a vCard
         */
        val vcard: String? = null
)


/**
 *  This object represents an animated emoji that displays a random value.
 */
data class Dice(

        /**
         *  Emoji on which the dice throw animation is based
         */
        val emoji: String,

        /**
         *  Value of the dice, 1-6 for “” and “” base emoji, 1-5 for “” base emoji
         */
        val value: Int
)


/**
 *  This object contains information about one answer option in a poll.
 */
data class PollOption(

        /**
         *  Option text, 1-100 characters
         */
        val text: String,

        /**
         *  Number of users that voted for this option
         */
        val voterCount: Int
)


/**
 *  This object represents an answer of a user in a non-anonymous poll.
 */
data class PollAnswer(

        /**
         *  Unique poll identifier
         */
        val pollId: String,

        /**
         *  The user, who changed the answer to the poll
         */
        val user: User,

        /**
         *  0-based identifiers of answer options, chosen by the user. May be empty if the user retracted their vote.
         */
        val optionIds: List<Int>
)


/**
 *  This object contains information about a poll.
 */
data class Poll(

        /**
         *  Unique poll identifier
         */
        val id: String,

        /**
         *  Poll question, 1-255 characters
         */
        val question: String,

        /**
         *  List of poll options
         */
        val options: List<PollOption>,

        /**
         *  Total number of users that voted in the poll
         */
        val totalVoterCount: Int,

        /**
         *  True, if the poll is closed
         */
        val isClosed: Boolean,

        /**
         *  True, if the poll is anonymous
         */
        val isAnonymous: Boolean,

        /**
         *  Poll type, currently can be “regular” or “quiz”
         */
        val type: String,

        /**
         *  True, if the poll allows multiple answers
         */
        val allowsMultipleAnswers: Boolean,

        /**
         *  0-based identifier of the correct answer option. Available only for polls in the quiz mode, which are
         *  closed, or was sent (not forwarded) by the bot or to the private chat with the bot.
         */
        val correctOptionId: Int? = null,

        /**
         *  Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll,
         *  0-200 characters
         */
        val explanation: String? = null,

        /**
         *  Special entities like usernames, URLs, bot commands, etc. that appear in the explanation
         */
        val explanationEntities: List<MessageEntity>? = null,

        /**
         *  Amount of time in seconds the poll will be active after creation
         */
        val openPeriod: Int? = null,

        /**
         *  Point in time (Unix timestamp) when the poll will be automatically closed
         */
        val closeDate: Int? = null
)


/**
 *  This object represents a point on the map.
 */
data class Location(

        /**
         *  Longitude as defined by sender
         */
        val longitude: Float,

        /**
         *  Latitude as defined by sender
         */
        val latitude: Float
)


/**
 *  This object represents a venue.
 */
data class Venue(

        /**
         *  Venue location
         */
        val location: Location,

        /**
         *  Name of the venue
         */
        val title: String,

        /**
         *  Address of the venue
         */
        val address: String,

        /**
         *  Foursquare identifier of the venue
         */
        val foursquareId: String? = null,

        /**
         *  Foursquare type of the venue. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or
         *  “food/icecream”.)
         */
        val foursquareType: String? = null
)


/**
 *  This object represent a user's profile pictures.
 */
data class UserProfilePhotos(

        /**
         *  Total number of profile pictures the target user has
         */
        val totalCount: Int,

        /**
         *  Requested profile pictures (in up to 4 sizes each)
         */
        val photos: List<List<PhotoSize>>
)


/**
 *  This object represents a file ready to be downloaded. The file can be downloaded via the link
 *  https://api.telegram.org/file/bot<token>/<file_path>. It is guaranteed that the link will be valid for at least 1
 *  hour. When the link expires, a new one can be requested by calling getFile.
 */
data class File(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  File size, if known
         */
        val fileSize: Int? = null,

        /**
         *  File path. Use https://api.telegram.org/file/bot<token>/<file_path> to get the file.
         */
        val filePath: String? = null
)


/**
 *  This object represents a custom keyboard with reply options (see Introduction to bots for details and examples).
 */
data class ReplyKeyboardMarkup(

        /**
         *  Array of button rows, each represented by an Array of KeyboardButton objects
         */
        val keyboard: List<List<KeyboardButton>>,

        /**
         *  Requests clients to resize the keyboard vertically for optimal fit (e.g., make the keyboard smaller if
         *  there are just two rows of buttons). Defaults to false, in which case the custom keyboard is always of the
         *  same height as the app's standard keyboard.
         */
        val resizeKeyboard: Boolean? = null,

        /**
         *  Requests clients to hide the keyboard as soon as it's been used. The keyboard will still be available, but
         *  clients will automatically display the usual letter-keyboard in the chat – the user can press a special
         *  button in the input field to see the custom keyboard again. Defaults to false.
         */
        val oneTimeKeyboard: Boolean? = null,

        /**
         *  Use this parameter if you want to show the keyboard to specific users only. Targets: 1) users that are
         *  @mentioned in the text of the Message object; 2) if the bot's message is a reply (has reply_to_message_id),
         *  sender of the original message.Example: A user requests to change the bot's language, bot replies to the
         *  request with a keyboard to select the new language. Other users in the group don't see the keyboard.
         */
        val selective: Boolean? = null
) : Markup


/**
 *  This object represents one button of the reply keyboard. For simple text buttons String can be used instead of this
 *  object to specify text of the button. Optional fields request_contact, request_location, and request_poll are
 *  mutually exclusive.
Note: request_contact and request_location options will only work in Telegram versions released
 *  after 9 April, 2016. Older clients will display unsupported message.Note: request_poll option will only work in
 *  Telegram versions released after 23 January, 2020. Older clients will display unsupported message.
 */
data class KeyboardButton(

        /**
         *  Text of the button. If none of the optional fields are used, it will be sent as a message when the button
         *  is pressed
         */
        val text: String,

        /**
         *  If True, the user's phone number will be sent as a contact when the button is pressed. Available in private
         *  chats only
         */
        val requestContact: Boolean? = null,

        /**
         *  If True, the user's current location will be sent when the button is pressed. Available in private chats
         *  only
         */
        val requestLocation: Boolean? = null,

        /**
         *  If specified, the user will be asked to create a poll and send it to the bot when the button is pressed.
         *  Available in private chats only
         */
        val requestPoll: KeyboardButtonPollType? = null
)


/**
 *  This object represents type of a poll, which is allowed to be created and sent when the corresponding button is
 *  pressed.
 */
data class KeyboardButtonPollType(

        /**
         *  If quiz is passed, the user will be allowed to create only polls in the quiz mode. If regular is passed,
         *  only regular polls will be allowed. Otherwise, the user will be allowed to create a poll of any type.
         */
        val type: String? = null
)


/**
 *  Upon receiving a message with this object, Telegram clients will remove the current custom keyboard and display the
 *  default letter-keyboard. By default, custom keyboards are displayed until a new keyboard is sent by a bot. An
 *  exception is made for one-time keyboards that are hidden immediately after the user presses a button (see
 *  ReplyKeyboardMarkup).
 */
data class ReplyKeyboardRemove(

        /**
         *  Requests clients to remove the custom keyboard (user will not be able to summon this keyboard; if you want
         *  to hide the keyboard from sight but keep it accessible, use one_time_keyboard in ReplyKeyboardMarkup)
         */
        val removeKeyboard: Boolean,

        /**
         *  Use this parameter if you want to remove the keyboard for specific users only. Targets: 1) users that are
         *  @mentioned in the text of the Message object; 2) if the bot's message is a reply (has reply_to_message_id),
         *  sender of the original message.Example: A user votes in a poll, bot returns confirmation message in reply
         *  to the vote and removes the keyboard for that user, while still showing the keyboard with poll options to
         *  users who haven't voted yet.
         */
        val selective: Boolean? = null
) : Markup


/**
 *  This object represents an inline keyboard that appears right next to the message it belongs to.
Note: This will
 *  only work in Telegram versions released after 9 April, 2016. Older clients will display unsupported message.
 */
data class InlineKeyboardMarkup(

        /**
         *  Array of button rows, each represented by an Array of InlineKeyboardButton objects
         */
        val inlineKeyboard: List<List<InlineKeyboardButton>>
) : Markup


/**
 *  This object represents one button of an inline keyboard. You must use exactly one of the optional fields.
 */
data class InlineKeyboardButton(

        /**
         *  Label text on the button
         */
        val text: String,

        /**
         *  HTTP or tg:// url to be opened when button is pressed
         */
        val url: String? = null,

        /**
         *  An HTTP URL used to automatically authorize the user. Can be used as a replacement for the Telegram Login
         *  Widget.
         */
        val loginUrl: LoginUrl? = null,

        /**
         *  Data to be sent in a callback query to the bot when button is pressed, 1-64 bytes
         */
        val callbackData: String? = null,

        /**
         *  If set, pressing the button will prompt the user to select one of their chats, open that chat and insert
         *  the bot's username and the specified inline query in the input field. Can be empty, in which case just the
         *  bot's username will be inserted.Note: This offers an easy way for users to start using your bot in inline
         *  mode when they are currently in a private chat with it. Especially useful when combined with switch_pm…
         *  actions – in this case the user will be automatically returned to the chat they switched from, skipping the
         *  chat selection screen.
         */
        val switchInlineQuery: String? = null,

        /**
         *  If set, pressing the button will insert the bot's username and the specified inline query in the current
         *  chat's input field. Can be empty, in which case only the bot's username will be inserted.This offers a
         *  quick way for the user to open your bot in inline mode in the same chat – good for selecting something from
         *  multiple options.
         */
        val switchInlineQueryCurrentChat: String? = null,

        /**
         *  Description of the game that will be launched when the user presses the button.NOTE: This type of button
         *  must always be the first button in the first row.
         */
        val callbackGame: CallbackGame? = null,

        /**
         *  Specify True, to send a Pay button.NOTE: This type of button must always be the first button in the first
         *  row.
         */
        val pay: Boolean? = null
)


/**
 *  This object represents a parameter of the inline keyboard button used to automatically authorize a user. Serves as
 *  a great replacement for the Telegram Login Widget when the user is coming from Telegram. All the user needs to do
 *  is tap/click a button and confirm that they want to log in:
Telegram apps support these buttons as of version 5.7.
 */
data class LoginUrl(

        /**
         *  An HTTP URL to be opened with user authorization data added to the query string when the button is pressed.
         *  If the user refuses to provide authorization data, the original URL without information about the user will
         *  be opened. The data added is the same as described in Receiving authorization data.NOTE: You must always
         *  check the hash of the received data to verify the authentication and the integrity of the data as described
         *  in Checking authorization.
         */
        val url: String,

        /**
         *  New text of the button in forwarded messages.
         */
        val forwardText: String? = null,

        /**
         *  Username of a bot, which will be used for user authorization. See Setting up a bot for more details. If not
         *  specified, the current bot's username will be assumed. The url's domain must be the same as the domain
         *  linked with the bot. See Linking your domain to the bot for more details.
         */
        val botUsername: String? = null,

        /**
         *  Pass True to request the permission for your bot to send messages to the user.
         */
        val requestWriteAccess: Boolean? = null
)


/**
 *  This object represents an incoming callback query from a callback button in an inline keyboard. If the button that
 *  originated the query was attached to a message sent by the bot, the field message will be present. If the button
 *  was attached to a message sent via the bot (in inline mode), the field inline_message_id will be present. Exactly
 *  one of the fields data or game_short_name will be present.
 */
data class CallbackQuery(

        /**
         *  Unique identifier for this query
         */
        val id: String,

        /**
         *  Sender
         */
        val from: User,

        /**
         *  Message with the callback button that originated the query. Note that message content and message date will
         *  not be available if the message is too old
         */
        val message: Message? = null,

        /**
         *  Identifier of the message sent via the bot in inline mode, that originated the query.
         */
        val inlineMessageId: String? = null,

        /**
         *  Global identifier, uniquely corresponding to the chat to which the message with the callback button was
         *  sent. Useful for high scores in games.
         */
        val chatInstance: String,

        /**
         *  Data associated with the callback button. Be aware that a bad client can send arbitrary data in this field.
         */
        val data: String? = null,

        /**
         *  Short name of a Game to be returned, serves as the unique identifier for the game
         */
        val gameShortName: String? = null
)


/**
 *  Upon receiving a message with this object, Telegram clients will display a reply interface to the user (act as if
 *  the user has selected the bot's message and tapped 'Reply'). This can be extremely useful if you want to create
 *  user-friendly step-by-step interfaces without having to sacrifice privacy mode.
 */
data class ForceReply(

        /**
         *  Shows reply interface to the user, as if they manually selected the bot's message and tapped 'Reply'
         */
        val forceReply: Boolean,

        /**
         *  Use this parameter if you want to force reply from specific users only. Targets: 1) users that are
         *  @mentioned in the text of the Message object; 2) if the bot's message is a reply (has reply_to_message_id),
         *  sender of the original message.
         */
        val selective: Boolean? = null
) : Markup


/**
 *  This object represents a chat photo.
 */
data class ChatPhoto(

        /**
         *  File identifier of small (160x160) chat photo. This file_id can be used only for photo download and only
         *  for as long as the photo is not changed.
         */
        val smallFileId: String,

        /**
         *  Unique file identifier of small (160x160) chat photo, which is supposed to be the same over time and for
         *  different bots. Can't be used to download or reuse the file.
         */
        val smallFileUniqueId: String,

        /**
         *  File identifier of big (640x640) chat photo. This file_id can be used only for photo download and only for
         *  as long as the photo is not changed.
         */
        val bigFileId: String,

        /**
         *  Unique file identifier of big (640x640) chat photo, which is supposed to be the same over time and for
         *  different bots. Can't be used to download or reuse the file.
         */
        val bigFileUniqueId: String
)


/**
 *  This object contains information about one member of a chat.
 */
data class ChatMember(

        /**
         *  Information about the user
         */
        val user: User,

        /**
         *  The member's status in the chat. Can be “creator”, “administrator”, “member”, “restricted”, “left” or
         *  “kicked”
         */
        val status: String,

        /**
         *  Owner and administrators only. Custom title for this user
         */
        val customTitle: String? = null,

        /**
         *  Restricted and kicked only. Date when restrictions will be lifted for this user; unix time
         */
        val untilDate: Int? = null,

        /**
         *  Administrators only. True, if the bot is allowed to edit administrator privileges of that user
         */
        val canBeEdited: Boolean? = null,

        /**
         *  Administrators only. True, if the administrator can post in the channel; channels only
         */
        val canPostMessages: Boolean? = null,

        /**
         *  Administrators only. True, if the administrator can edit messages of other users and can pin messages;
         *  channels only
         */
        val canEditMessages: Boolean? = null,

        /**
         *  Administrators only. True, if the administrator can delete messages of other users
         */
        val canDeleteMessages: Boolean? = null,

        /**
         *  Administrators only. True, if the administrator can restrict, ban or unban chat members
         */
        val canRestrictMembers: Boolean? = null,

        /**
         *  Administrators only. True, if the administrator can add new administrators with a subset of their own
         *  privileges or demote administrators that he has promoted, directly or indirectly (promoted by
         *  administrators that were appointed by the user)
         */
        val canPromoteMembers: Boolean? = null,

        /**
         *  Administrators and restricted only. True, if the user is allowed to change the chat title, photo and other
         *  settings
         */
        val canChangeInfo: Boolean? = null,

        /**
         *  Administrators and restricted only. True, if the user is allowed to invite new users to the chat
         */
        val canInviteUsers: Boolean? = null,

        /**
         *  Administrators and restricted only. True, if the user is allowed to pin messages; groups and supergroups
         *  only
         */
        val canPinMessages: Boolean? = null,

        /**
         *  Restricted only. True, if the user is a member of the chat at the moment of the request
         */
        val isMember: Boolean? = null,

        /**
         *  Restricted only. True, if the user is allowed to send text messages, contacts, locations and venues
         */
        val canSendMessages: Boolean? = null,

        /**
         *  Restricted only. True, if the user is allowed to send audios, documents, photos, videos, video notes and
         *  voice notes
         */
        val canSendMediaMessages: Boolean? = null,

        /**
         *  Restricted only. True, if the user is allowed to send polls
         */
        val canSendPolls: Boolean? = null,

        /**
         *  Restricted only. True, if the user is allowed to send animations, games, stickers and use inline bots
         */
        val canSendOtherMessages: Boolean? = null,

        /**
         *  Restricted only. True, if the user is allowed to add web page previews to their messages
         */
        val canAddWebPagePreviews: Boolean? = null
)


/**
 *  Describes actions that a non-administrator user is allowed to take in a chat.
 */
data class ChatPermissions(

        /**
         *  True, if the user is allowed to send text messages, contacts, locations and venues
         */
        val canSendMessages: Boolean? = null,

        /**
         *  True, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes,
         *  implies can_send_messages
         */
        val canSendMediaMessages: Boolean? = null,

        /**
         *  True, if the user is allowed to send polls, implies can_send_messages
         */
        val canSendPolls: Boolean? = null,

        /**
         *  True, if the user is allowed to send animations, games, stickers and use inline bots, implies
         *  can_send_media_messages
         */
        val canSendOtherMessages: Boolean? = null,

        /**
         *  True, if the user is allowed to add web page previews to their messages, implies can_send_media_messages
         */
        val canAddWebPagePreviews: Boolean? = null,

        /**
         *  True, if the user is allowed to change the chat title, photo and other settings. Ignored in public
         *  supergroups
         */
        val canChangeInfo: Boolean? = null,

        /**
         *  True, if the user is allowed to invite new users to the chat
         */
        val canInviteUsers: Boolean? = null,

        /**
         *  True, if the user is allowed to pin messages. Ignored in public supergroups
         */
        val canPinMessages: Boolean? = null
)


/**
 *  This object represents a bot command.
 */
data class BotCommand(

        /**
         *  Text of the command, 1-32 characters. Can contain only lowercase English letters, digits and underscores.
         */
        val command: String,

        /**
         *  Description of the command, 3-256 characters.
         */
        val description: String
)


/**
 *  Contains information about why a request was unsuccessful.
 */
data class ResponseParameters(

        /**
         *  The group has been migrated to a supergroup with the specified identifier. This number may be greater than
         *  32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is
         *  smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this
         *  identifier.
         */
        val migrateToChatId: Long? = null,

        /**
         *  In case of exceeding flood control, the number of seconds left to wait before the request can be repeated
         */
        val retryAfter: Int? = null
)


/**
 *  This object represents the content of a media message to be sent. It should be one of
 */
interface InputMedia


/**
 *  Represents a photo to be sent.
 */
data class InputMediaPhoto(

        /**
         *  Type of the result, must be photo
         */
        val type: String,

        /**
         *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP
         *  URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one
         *  using multipart/form-data under <file_attach_name> name. More info on Sending Files »
         */
        val media: String,

        /**
         *  Caption of the photo to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the photo caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null
) : InputMedia


/**
 *  Represents a video to be sent.
 */
data class InputMediaVideo(

        /**
         *  Type of the result, must be video
         */
        val type: String,

        /**
         *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP
         *  URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one
         *  using multipart/form-data under <file_attach_name> name. More info on Sending Files »
         */
        val media: String,

        /**
         *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
         *  The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should
         *  not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused
         *  and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was
         *  uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
         */
        val thumb: InputFile? = null,

        /**
         *  Caption of the video to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the video caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Video width
         */
        val width: Int? = null,

        /**
         *  Video height
         */
        val height: Int? = null,

        /**
         *  Video duration
         */
        val duration: Int? = null,

        /**
         *  Pass True, if the uploaded video is suitable for streaming
         */
        val supportsStreaming: Boolean? = null
) : InputMedia


/**
 *  Represents an animation file (GIF or H.264/MPEG-4 AVC video without sound) to be sent.
 */
data class InputMediaAnimation(

        /**
         *  Type of the result, must be animation
         */
        val type: String,

        /**
         *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP
         *  URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one
         *  using multipart/form-data under <file_attach_name> name. More info on Sending Files »
         */
        val media: String,

        /**
         *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
         *  The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should
         *  not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused
         *  and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was
         *  uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
         */
        val thumb: InputFile? = null,

        /**
         *  Caption of the animation to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the animation caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Animation width
         */
        val width: Int? = null,

        /**
         *  Animation height
         */
        val height: Int? = null,

        /**
         *  Animation duration
         */
        val duration: Int? = null
)


/**
 *  Represents an audio file to be treated as music to be sent.
 */
data class InputMediaAudio(

        /**
         *  Type of the result, must be audio
         */
        val type: String,

        /**
         *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP
         *  URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one
         *  using multipart/form-data under <file_attach_name> name. More info on Sending Files »
         */
        val media: String,

        /**
         *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
         *  The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should
         *  not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused
         *  and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was
         *  uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
         */
        val thumb: InputFile? = null,

        /**
         *  Caption of the audio to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the audio caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Duration of the audio in seconds
         */
        val duration: Int? = null,

        /**
         *  Performer of the audio
         */
        val performer: String? = null,

        /**
         *  Title of the audio
         */
        val title: String? = null
)


/**
 *  Represents a general file to be sent.
 */
data class InputMediaDocument(

        /**
         *  Type of the result, must be document
         */
        val type: String,

        /**
         *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP
         *  URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one
         *  using multipart/form-data under <file_attach_name> name. More info on Sending Files »
         */
        val media: String,

        /**
         *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
         *  The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should
         *  not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused
         *  and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was
         *  uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
         */
        val thumb: InputFile? = null,

        /**
         *  Caption of the document to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the document caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null
)


/**
 *  This object represents the contents of a file to be uploaded. Must be posted using multipart/form-data in the usual
 *  way that files are uploaded via the browser.
 */
class InputFile


/**
 *  This object represents a sticker.
 */
data class Sticker(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  Sticker width
         */
        val width: Int,

        /**
         *  Sticker height
         */
        val height: Int,

        /**
         *  True, if the sticker is animated
         */
        val isAnimated: Boolean,

        /**
         *  Sticker thumbnail in the .WEBP or .JPG format
         */
        val thumb: PhotoSize? = null,

        /**
         *  Emoji associated with the sticker
         */
        val emoji: String? = null,

        /**
         *  Name of the sticker set to which the sticker belongs
         */
        val setName: String? = null,

        /**
         *  For mask stickers, the position where the mask should be placed
         */
        val maskPosition: MaskPosition? = null,

        /**
         *  File size
         */
        val fileSize: Int? = null
)


/**
 *  This object represents a sticker set.
 */
data class StickerSet(

        /**
         *  Sticker set name
         */
        val name: String,

        /**
         *  Sticker set title
         */
        val title: String,

        /**
         *  True, if the sticker set contains animated stickers
         */
        val isAnimated: Boolean,

        /**
         *  True, if the sticker set contains masks
         */
        val containsMasks: Boolean,

        /**
         *  List of all set stickers
         */
        val stickers: List<Sticker>,

        /**
         *  Sticker set thumbnail in the .WEBP or .TGS format
         */
        val thumb: PhotoSize? = null
)


/**
 *  This object describes the position on faces where a mask should be placed by default.
 */
data class MaskPosition(

        /**
         *  The part of the face relative to which the mask should be placed. One of “forehead”, “eyes”, “mouth”, or
         *  “chin”.
         */
        val point: String,

        /**
         *  Shift by X-axis measured in widths of the mask scaled to the face size, from left to right. For example,
         *  choosing -1.0 will place mask just to the left of the default mask position.
         */
        val xShift: Double,

        /**
         *  Shift by Y-axis measured in heights of the mask scaled to the face size, from top to bottom. For example,
         *  1.0 will place the mask just below the default mask position.
         */
        val yShift: Double,

        /**
         *  Mask scaling coefficient. For example, 2.0 means double size.
         */
        val scale: Double
)


/**
 *  This object represents an incoming inline query. When the user sends an empty query, your bot could return some
 *  default or trending results.
 */
data class InlineQuery(

        /**
         *  Unique identifier for this query
         */
        val id: String,

        /**
         *  Sender
         */
        val from: User,

        /**
         *  Sender location, only for bots that request user location
         */
        val location: Location? = null,

        /**
         *  Text of the query (up to 256 characters)
         */
        val query: String,

        /**
         *  Offset of the results to be returned, can be controlled by the bot
         */
        val offset: String
)


/**
 *  This object represents one result of an inline query. Telegram clients currently support results of the following
 *  20 types:
 */
class InlineQueryResult


/**
 *  Represents a link to an article or web page.
 */
data class InlineQueryResultArticle(

        /**
         *  Type of the result, must be article
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 Bytes
         */
        val id: String,

        /**
         *  Title of the result
         */
        val title: String,

        /**
         *  Content of the message to be sent
         */
        val inputMessageContent: InputMessageContent,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  URL of the result
         */
        val url: String? = null,

        /**
         *  Pass True, if you don't want the URL to be shown in the message
         */
        val hideUrl: Boolean? = null,

        /**
         *  Short description of the result
         */
        val description: String? = null,

        /**
         *  Url of the thumbnail for the result
         */
        val thumbUrl: String? = null,

        /**
         *  Thumbnail width
         */
        val thumbWidth: Int? = null,

        /**
         *  Thumbnail height
         */
        val thumbHeight: Int? = null
)


/**
 *  Represents a link to a photo. By default, this photo will be sent by the user with optional caption. Alternatively,
 *  you can use input_message_content to send a message with the specified content instead of the photo.
 */
data class InlineQueryResultPhoto(

        /**
         *  Type of the result, must be photo
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid URL of the photo. Photo must be in jpeg format. Photo size must not exceed 5MB
         */
        val photoUrl: String,

        /**
         *  URL of the thumbnail for the photo
         */
        val thumbUrl: String,

        /**
         *  Width of the photo
         */
        val photoWidth: Int? = null,

        /**
         *  Height of the photo
         */
        val photoHeight: Int? = null,

        /**
         *  Title for the result
         */
        val title: String? = null,

        /**
         *  Short description of the result
         */
        val description: String? = null,

        /**
         *  Caption of the photo to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the photo caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the photo
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to an animated GIF file. By default, this animated GIF file will be sent by the user with
 *  optional caption. Alternatively, you can use input_message_content to send a message with the specified content
 *  instead of the animation.
 */
data class InlineQueryResultGif(

        /**
         *  Type of the result, must be gif
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid URL for the GIF file. File size must not exceed 1MB
         */
        val gifUrl: String,

        /**
         *  Width of the GIF
         */
        val gifWidth: Int? = null,

        /**
         *  Height of the GIF
         */
        val gifHeight: Int? = null,

        /**
         *  Duration of the GIF
         */
        val gifDuration: Int? = null,

        /**
         *  URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
         */
        val thumbUrl: String,

        /**
         *  MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to
         *  “image/jpeg”
         */
        val thumbMimeType: String? = null,

        /**
         *  Title for the result
         */
        val title: String? = null,

        /**
         *  Caption of the GIF file to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the GIF animation
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4
 *  file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a
 *  message with the specified content instead of the animation.
 */
data class InlineQueryResultMpeg4Gif(

        /**
         *  Type of the result, must be mpeg4_gif
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid URL for the MP4 file. File size must not exceed 1MB
         */
        val mpeg4Url: String,

        /**
         *  Video width
         */
        val mpeg4Width: Int? = null,

        /**
         *  Video height
         */
        val mpeg4Height: Int? = null,

        /**
         *  Video duration
         */
        val mpeg4Duration: Int? = null,

        /**
         *  URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
         */
        val thumbUrl: String,

        /**
         *  MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to
         *  “image/jpeg”
         */
        val thumbMimeType: String? = null,

        /**
         *  Title for the result
         */
        val title: String? = null,

        /**
         *  Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the video animation
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a page containing an embedded video player or a video file. By default, this video file will
 *  be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message
 *  with the specified content instead of the video.
 */
data class InlineQueryResultVideo(

        /**
         *  Type of the result, must be video
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid URL for the embedded video player or video file
         */
        val videoUrl: String,

        /**
         *  Mime type of the content of video url, “text/html” or “video/mp4”
         */
        val mimeType: String,

        /**
         *  URL of the thumbnail (jpeg only) for the video
         */
        val thumbUrl: String,

        /**
         *  Title for the result
         */
        val title: String,

        /**
         *  Caption of the video to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the video caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Video width
         */
        val videoWidth: Int? = null,

        /**
         *  Video height
         */
        val videoHeight: Int? = null,

        /**
         *  Video duration in seconds
         */
        val videoDuration: Int? = null,

        /**
         *  Short description of the result
         */
        val description: String? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the video. This field is required if InlineQueryResultVideo is
         *  used to send an HTML-page as a result (e.g., a YouTube video).
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to an MP3 audio file. By default, this audio file will be sent by the user. Alternatively, you
 *  can use input_message_content to send a message with the specified content instead of the audio.
Note: This will
 *  only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
 */
data class InlineQueryResultAudio(

        /**
         *  Type of the result, must be audio
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid URL for the audio file
         */
        val audioUrl: String,

        /**
         *  Title
         */
        val title: String,

        /**
         *  Caption, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the audio caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Performer
         */
        val performer: String? = null,

        /**
         *  Audio duration in seconds
         */
        val audioDuration: Int? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the audio
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a voice recording in an .OGG container encoded with OPUS. By default, this voice recording
 *  will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified
 *  content instead of the the voice message.
Note: This will only work in Telegram versions released after 9 April,
 *  2016. Older clients will ignore them.
 */
data class InlineQueryResultVoice(

        /**
         *  Type of the result, must be voice
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid URL for the voice recording
         */
        val voiceUrl: String,

        /**
         *  Recording title
         */
        val title: String,

        /**
         *  Caption, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the voice message caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Recording duration in seconds
         */
        val voiceDuration: Int? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the voice recording
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a file. By default, this file will be sent by the user with an optional caption.
 *  Alternatively, you can use input_message_content to send a message with the specified content instead of the file.
 *  Currently, only .PDF and .ZIP files can be sent using this method.
Note: This will only work in Telegram versions
 *  released after 9 April, 2016. Older clients will ignore them.
 */
data class InlineQueryResultDocument(

        /**
         *  Type of the result, must be document
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  Title for the result
         */
        val title: String,

        /**
         *  Caption of the document to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the document caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  A valid URL for the file
         */
        val documentUrl: String,

        /**
         *  Mime type of the content of the file, either “application/pdf” or “application/zip”
         */
        val mimeType: String,

        /**
         *  Short description of the result
         */
        val description: String? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the file
         */
        val inputMessageContent: InputMessageContent? = null,

        /**
         *  URL of the thumbnail (jpeg only) for the file
         */
        val thumbUrl: String? = null,

        /**
         *  Thumbnail width
         */
        val thumbWidth: Int? = null,

        /**
         *  Thumbnail height
         */
        val thumbHeight: Int? = null
)


/**
 *  Represents a location on a map. By default, the location will be sent by the user. Alternatively, you can use
 *  input_message_content to send a message with the specified content instead of the location.
Note: This will only
 *  work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
 */
data class InlineQueryResultLocation(

        /**
         *  Type of the result, must be location
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 Bytes
         */
        val id: String,

        /**
         *  Location latitude in degrees
         */
        val latitude: Double,

        /**
         *  Location longitude in degrees
         */
        val longitude: Double,

        /**
         *  Location title
         */
        val title: String,

        /**
         *  Period in seconds for which the location can be updated, should be between 60 and 86400.
         */
        val livePeriod: Int? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the location
         */
        val inputMessageContent: InputMessageContent? = null,

        /**
         *  Url of the thumbnail for the result
         */
        val thumbUrl: String? = null,

        /**
         *  Thumbnail width
         */
        val thumbWidth: Int? = null,

        /**
         *  Thumbnail height
         */
        val thumbHeight: Int? = null
)


/**
 *  Represents a venue. By default, the venue will be sent by the user. Alternatively, you can use
 *  input_message_content to send a message with the specified content instead of the venue.
Note: This will only work
 *  in Telegram versions released after 9 April, 2016. Older clients will ignore them.
 */
data class InlineQueryResultVenue(

        /**
         *  Type of the result, must be venue
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 Bytes
         */
        val id: String,

        /**
         *  Latitude of the venue location in degrees
         */
        val latitude: Float,

        /**
         *  Longitude of the venue location in degrees
         */
        val longitude: Float,

        /**
         *  Title of the venue
         */
        val title: String,

        /**
         *  Address of the venue
         */
        val address: String,

        /**
         *  Foursquare identifier of the venue if known
         */
        val foursquareId: String? = null,

        /**
         *  Foursquare type of the venue, if known. (For example, “arts_entertainment/default”,
         *  “arts_entertainment/aquarium” or “food/icecream”.)
         */
        val foursquareType: String? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the venue
         */
        val inputMessageContent: InputMessageContent? = null,

        /**
         *  Url of the thumbnail for the result
         */
        val thumbUrl: String? = null,

        /**
         *  Thumbnail width
         */
        val thumbWidth: Int? = null,

        /**
         *  Thumbnail height
         */
        val thumbHeight: Int? = null
)


/**
 *  Represents a contact with a phone number. By default, this contact will be sent by the user. Alternatively, you can
 *  use input_message_content to send a message with the specified content instead of the contact.
Note: This will only
 *  work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
 */
data class InlineQueryResultContact(

        /**
         *  Type of the result, must be contact
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 Bytes
         */
        val id: String,

        /**
         *  Contact's phone number
         */
        val phoneNumber: String,

        /**
         *  Contact's first name
         */
        val firstName: String,

        /**
         *  Contact's last name
         */
        val lastName: String? = null,

        /**
         *  Additional data about the contact in the form of a vCard, 0-2048 bytes
         */
        val vcard: String? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the contact
         */
        val inputMessageContent: InputMessageContent? = null,

        /**
         *  Url of the thumbnail for the result
         */
        val thumbUrl: String? = null,

        /**
         *  Thumbnail width
         */
        val thumbWidth: Int? = null,

        /**
         *  Thumbnail height
         */
        val thumbHeight: Int? = null
)


/**
 *  Represents a Game.
Note: This will only work in Telegram versions released after October 1, 2016. Older clients
 *  will not display any inline results if a game result is among them.
 */
data class InlineQueryResultGame(

        /**
         *  Type of the result, must be game
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  Short name of the game
         */
        val gameShortName: String,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  Represents a link to a photo stored on the Telegram servers. By default, this photo will be sent by the user with
 *  an optional caption. Alternatively, you can use input_message_content to send a message with the specified content
 *  instead of the photo.
 */
data class InlineQueryResultCachedPhoto(

        /**
         *  Type of the result, must be photo
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid file identifier of the photo
         */
        val photoFileId: String,

        /**
         *  Title for the result
         */
        val title: String? = null,

        /**
         *  Short description of the result
         */
        val description: String? = null,

        /**
         *  Caption of the photo to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the photo caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the photo
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to an animated GIF file stored on the Telegram servers. By default, this animated GIF file will
 *  be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message
 *  with specified content instead of the animation.
 */
data class InlineQueryResultCachedGif(

        /**
         *  Type of the result, must be gif
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid file identifier for the GIF file
         */
        val gifFileId: String,

        /**
         *  Title for the result
         */
        val title: String? = null,

        /**
         *  Caption of the GIF file to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the GIF animation
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a video animation (H.264/MPEG-4 AVC video without sound) stored on the Telegram servers. By
 *  default, this animated MPEG-4 file will be sent by the user with an optional caption. Alternatively, you can use
 *  input_message_content to send a message with the specified content instead of the animation.
 */
data class InlineQueryResultCachedMpeg4Gif(

        /**
         *  Type of the result, must be mpeg4_gif
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid file identifier for the MP4 file
         */
        val mpeg4FileId: String,

        /**
         *  Title for the result
         */
        val title: String? = null,

        /**
         *  Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the video animation
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a sticker stored on the Telegram servers. By default, this sticker will be sent by the user.
 *  Alternatively, you can use input_message_content to send a message with the specified content instead of the
 *  sticker.
Note: This will only work in Telegram versions released after 9 April, 2016 for static stickers and after
 *  06 July, 2019 for animated stickers. Older clients will ignore them.
 */
data class InlineQueryResultCachedSticker(

        /**
         *  Type of the result, must be sticker
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid file identifier of the sticker
         */
        val stickerFileId: String,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the sticker
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a file stored on the Telegram servers. By default, this file will be sent by the user with an
 *  optional caption. Alternatively, you can use input_message_content to send a message with the specified content
 *  instead of the file.
Note: This will only work in Telegram versions released after 9 April, 2016. Older clients
 *  will ignore them.
 */
data class InlineQueryResultCachedDocument(

        /**
         *  Type of the result, must be document
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  Title for the result
         */
        val title: String,

        /**
         *  A valid file identifier for the file
         */
        val documentFileId: String,

        /**
         *  Short description of the result
         */
        val description: String? = null,

        /**
         *  Caption of the document to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the document caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the file
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a video file stored on the Telegram servers. By default, this video file will be sent by the
 *  user with an optional caption. Alternatively, you can use input_message_content to send a message with the
 *  specified content instead of the video.
 */
data class InlineQueryResultCachedVideo(

        /**
         *  Type of the result, must be video
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid file identifier for the video file
         */
        val videoFileId: String,

        /**
         *  Title for the result
         */
        val title: String,

        /**
         *  Short description of the result
         */
        val description: String? = null,

        /**
         *  Caption of the video to be sent, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the video caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the video
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to a voice message stored on the Telegram servers. By default, this voice message will be sent by
 *  the user. Alternatively, you can use input_message_content to send a message with the specified content instead of
 *  the voice message.
Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will
 *  ignore them.
 */
data class InlineQueryResultCachedVoice(

        /**
         *  Type of the result, must be voice
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid file identifier for the voice message
         */
        val voiceFileId: String,

        /**
         *  Voice message title
         */
        val title: String,

        /**
         *  Caption, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the voice message caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the voice message
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  Represents a link to an MP3 audio file stored on the Telegram servers. By default, this audio file will be sent by
 *  the user. Alternatively, you can use input_message_content to send a message with the specified content instead of
 *  the audio.
Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore
 *  them.
 */
data class InlineQueryResultCachedAudio(

        /**
         *  Type of the result, must be audio
         */
        val type: String,

        /**
         *  Unique identifier for this result, 1-64 bytes
         */
        val id: String,

        /**
         *  A valid file identifier for the audio file
         */
        val audioFileId: String,

        /**
         *  Caption, 0-1024 characters after entities parsing
         */
        val caption: String? = null,

        /**
         *  Mode for parsing entities in the audio caption. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Inline keyboard attached to the message
         */
        val replyMarkup: InlineKeyboardMarkup? = null,

        /**
         *  Content of the message to be sent instead of the audio
         */
        val inputMessageContent: InputMessageContent? = null
)


/**
 *  This object represents the content of a message to be sent as a result of an inline query. Telegram clients
 *  currently support the following 4 types:
 */
class InputMessageContent


/**
 *  Represents the content of a text message to be sent as the result of an inline query.
 */
data class InputTextMessageContent(

        /**
         *  Text of the message to be sent, 1-4096 characters
         */
        val messageText: String,

        /**
         *  Mode for parsing entities in the message text. See formatting options for more details.
         */
        val parseMode: ParseMode? = null,

        /**
         *  Disables link previews for links in the sent message
         */
        val disableWebPagePreview: Boolean? = null
)


/**
 *  Represents the content of a location message to be sent as the result of an inline query.
 */
data class InputLocationMessageContent(

        /**
         *  Latitude of the location in degrees
         */
        val latitude: Float,

        /**
         *  Longitude of the location in degrees
         */
        val longitude: Float,

        /**
         *  Period in seconds for which the location can be updated, should be between 60 and 86400.
         */
        val livePeriod: Int? = null
)


/**
 *  Represents the content of a venue message to be sent as the result of an inline query.
 */
data class InputVenueMessageContent(

        /**
         *  Latitude of the venue in degrees
         */
        val latitude: Float,

        /**
         *  Longitude of the venue in degrees
         */
        val longitude: Float,

        /**
         *  Name of the venue
         */
        val title: String,

        /**
         *  Address of the venue
         */
        val address: String,

        /**
         *  Foursquare identifier of the venue, if known
         */
        val foursquareId: String? = null,

        /**
         *  Foursquare type of the venue, if known. (For example, “arts_entertainment/default”,
         *  “arts_entertainment/aquarium” or “food/icecream”.)
         */
        val foursquareType: String? = null
)


/**
 *  Represents the content of a contact message to be sent as the result of an inline query.
 */
data class InputContactMessageContent(

        /**
         *  Contact's phone number
         */
        val phoneNumber: String,

        /**
         *  Contact's first name
         */
        val firstName: String,

        /**
         *  Contact's last name
         */
        val lastName: String? = null,

        /**
         *  Additional data about the contact in the form of a vCard, 0-2048 bytes
         */
        val vcard: String? = null
)


/**
 *  Represents a result of an inline query that was chosen by the user and sent to their chat partner.
Note: It is
 *  necessary to enable inline feedback via @Botfather in order to receive these objects in updates.
 */
data class ChosenInlineResult(

        /**
         *  The unique identifier for the result that was chosen
         */
        val resultId: String,

        /**
         *  The user that chose the result
         */
        val from: User,

        /**
         *  Sender location, only for bots that require user location
         */
        val location: Location? = null,

        /**
         *  Identifier of the sent inline message. Available only if there is an inline keyboard attached to the
         *  message. Will be also received in callback queries and can be used to edit the message.
         */
        val inlineMessageId: String? = null,

        /**
         *  The query that was used to obtain the result
         */
        val query: String
)


/**
 *  This object represents a portion of the price for goods or services.
 */
data class LabeledPrice(

        /**
         *  Portion label
         */
        val label: String,

        /**
         *  Price of the product in the smallest units of the currency (integer, not float/double). For example, for a
         *  price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of
         *  digits past the decimal point for each currency (2 for the majority of currencies).
         */
        val amount: Int
)


/**
 *  This object contains basic information about an invoice.
 */
data class Invoice(

        /**
         *  Product name
         */
        val title: String,

        /**
         *  Product description
         */
        val description: String,

        /**
         *  Unique bot deep-linking parameter that can be used to generate this invoice
         */
        val startParameter: String,

        /**
         *  Three-letter ISO 4217 currency code
         */
        val currency: String,

        /**
         *  Total price in the smallest units of the currency (integer, not float/double). For example, for a price of
         *  US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past
         *  the decimal point for each currency (2 for the majority of currencies).
         */
        val totalAmount: Int
)


/**
 *  This object represents a shipping address.
 */
data class ShippingAddress(

        /**
         *  ISO 3166-1 alpha-2 country code
         */
        val countryCode: String,

        /**
         *  State, if applicable
         */
        val state: String,

        /**
         *  City
         */
        val city: String,

        /**
         *  First line for the address
         */
        val streetLine1: String,

        /**
         *  Second line for the address
         */
        val streetLine2: String,

        /**
         *  Address post code
         */
        val postCode: String
)


/**
 *  This object represents information about an order.
 */
data class OrderInfo(

        /**
         *  User name
         */
        val name: String? = null,

        /**
         *  User's phone number
         */
        val phoneNumber: String? = null,

        /**
         *  User email
         */
        val email: String? = null,

        /**
         *  User shipping address
         */
        val shippingAddress: ShippingAddress? = null
)


/**
 *  This object represents one shipping option.
 */
data class ShippingOption(

        /**
         *  Shipping option identifier
         */
        val id: String,

        /**
         *  Option title
         */
        val title: String,

        /**
         *  List of price portions
         */
        val prices: List<LabeledPrice>
)


/**
 *  This object contains basic information about a successful payment.
 */
data class SuccessfulPayment(

        /**
         *  Three-letter ISO 4217 currency code
         */
        val currency: String,

        /**
         *  Total price in the smallest units of the currency (integer, not float/double). For example, for a price of
         *  US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past
         *  the decimal point for each currency (2 for the majority of currencies).
         */
        val totalAmount: Int,

        /**
         *  Bot specified invoice payload
         */
        val invoicePayload: String,

        /**
         *  Identifier of the shipping option chosen by the user
         */
        val shippingOptionId: String? = null,

        /**
         *  Order info provided by the user
         */
        val orderInfo: OrderInfo? = null,

        /**
         *  Telegram payment identifier
         */
        val telegramPaymentChargeId: String,

        /**
         *  Provider payment identifier
         */
        val providerPaymentChargeId: String
)


/**
 *  This object contains information about an incoming shipping query.
 */
data class ShippingQuery(

        /**
         *  Unique query identifier
         */
        val id: String,

        /**
         *  User who sent the query
         */
        val from: User,

        /**
         *  Bot specified invoice payload
         */
        val invoicePayload: String,

        /**
         *  User specified shipping address
         */
        val shippingAddress: ShippingAddress
)


/**
 *  This object contains information about an incoming pre-checkout query.
 */
data class PreCheckoutQuery(

        /**
         *  Unique query identifier
         */
        val id: String,

        /**
         *  User who sent the query
         */
        val from: User,

        /**
         *  Three-letter ISO 4217 currency code
         */
        val currency: String,

        /**
         *  Total price in the smallest units of the currency (integer, not float/double). For example, for a price of
         *  US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past
         *  the decimal point for each currency (2 for the majority of currencies).
         */
        val totalAmount: Int,

        /**
         *  Bot specified invoice payload
         */
        val invoicePayload: String,

        /**
         *  Identifier of the shipping option chosen by the user
         */
        val shippingOptionId: String? = null,

        /**
         *  Order info provided by the user
         */
        val orderInfo: OrderInfo? = null
)


/**
 *  Contains information about Telegram Passport data shared with the bot by the user.
 */
data class PassportData(

        /**
         *  Array with information about documents and other Telegram Passport elements that was shared with the bot
         */
        val data: List<EncryptedPassportElement>,

        /**
         *  Encrypted credentials required to decrypt the data
         */
        val credentials: EncryptedCredentials
)


/**
 *  This object represents a file uploaded to Telegram Passport. Currently all Telegram Passport files are in JPEG
 *  format when decrypted and don't exceed 10MB.
 */
data class PassportFile(

        /**
         *  Identifier for this file, which can be used to download or reuse the file
         */
        val fileId: String,

        /**
         *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't
         *  be used to download or reuse the file.
         */
        val fileUniqueId: String,

        /**
         *  File size
         */
        val fileSize: Int,

        /**
         *  Unix time when the file was uploaded
         */
        val fileDate: Int
)


/**
 *  Contains information about documents or other Telegram Passport elements shared with the bot by the user.
 */
data class EncryptedPassportElement(

        /**
         *  Element type. One of “personal_details”, “passport”, “driver_license”, “identity_card”,
         *  “internal_passport”, “address”, “utility_bill”, “bank_statement”, “rental_agreement”,
         *  “passport_registration”, “temporary_registration”, “phone_number”, “email”.
         */
        val type: String,

        /**
         *  Base64-encoded encrypted Telegram Passport element data provided by the user, available for
         *  “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport” and “address” types.
         *  Can be decrypted and verified using the accompanying EncryptedCredentials.
         */
        val data: String? = null,

        /**
         *  User's verified phone number, available only for “phone_number” type
         */
        val phoneNumber: String? = null,

        /**
         *  User's verified email address, available only for “email” type
         */
        val email: String? = null,

        /**
         *  Array of encrypted files with documents provided by the user, available for “utility_bill”,
         *  “bank_statement”, “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can
         *  be decrypted and verified using the accompanying EncryptedCredentials.
         */
        val files: List<PassportFile>? = null,

        /**
         *  Encrypted file with the front side of the document, provided by the user. Available for “passport”,
         *  “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the
         *  accompanying EncryptedCredentials.
         */
        val frontSide: PassportFile? = null,

        /**
         *  Encrypted file with the reverse side of the document, provided by the user. Available for “driver_license”
         *  and “identity_card”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
         */
        val reverseSide: PassportFile? = null,

        /**
         *  Encrypted file with the selfie of the user holding a document, provided by the user; available for
         *  “passport”, “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and
         *  verified using the accompanying EncryptedCredentials.
         */
        val selfie: PassportFile? = null,

        /**
         *  Array of encrypted files with translated versions of documents provided by the user. Available if requested
         *  for “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”,
         *  “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can be decrypted and
         *  verified using the accompanying EncryptedCredentials.
         */
        val translation: List<PassportFile>? = null,

        /**
         *  Base64-encoded element hash for using in PassportElementErrorUnspecified
         */
        val hash: String
)


/**
 *  Contains data required for decrypting and authenticating EncryptedPassportElement. See the Telegram Passport
 *  Documentation for a complete description of the data decryption and authentication processes.
 */
data class EncryptedCredentials(

        /**
         *  Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets required
         *  for EncryptedPassportElement decryption and authentication
         */
        val data: String,

        /**
         *  Base64-encoded data hash for data authentication
         */
        val hash: String,

        /**
         *  Base64-encoded secret, encrypted with the bot's public RSA key, required for data decryption
         */
        val secret: String
)


/**
 *  This object represents an error in the Telegram Passport element which was submitted that should be resolved by the
 *  user. It should be one of:
 */
class PassportElementError


/**
 *  Represents an issue in one of the data fields that was provided by the user. The error is considered resolved when
 *  the field's value changes.
 */
data class PassportElementErrorDataField(

        /**
         *  Error source, must be data
         */
        val source: String,

        /**
         *  The section of the user's Telegram Passport which has the error, one of “personal_details”, “passport”,
         *  “driver_license”, “identity_card”, “internal_passport”, “address”
         */
        val type: String,

        /**
         *  Name of the data field which has the error
         */
        val fieldName: String,

        /**
         *  Base64-encoded data hash
         */
        val dataHash: String,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  Represents an issue with the front side of a document. The error is considered resolved when the file with the
 *  front side of the document changes.
 */
data class PassportElementErrorFrontSide(

        /**
         *  Error source, must be front_side
         */
        val source: String,

        /**
         *  The section of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”,
         *  “identity_card”, “internal_passport”
         */
        val type: String,

        /**
         *  Base64-encoded hash of the file with the front side of the document
         */
        val fileHash: String,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  Represents an issue with the reverse side of a document. The error is considered resolved when the file with
 *  reverse side of the document changes.
 */
data class PassportElementErrorReverseSide(

        /**
         *  Error source, must be reverse_side
         */
        val source: String,

        /**
         *  The section of the user's Telegram Passport which has the issue, one of “driver_license”, “identity_card”
         */
        val type: String,

        /**
         *  Base64-encoded hash of the file with the reverse side of the document
         */
        val fileHash: String,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  Represents an issue with the selfie with a document. The error is considered resolved when the file with the selfie
 *  changes.
 */
data class PassportElementErrorSelfie(

        /**
         *  Error source, must be selfie
         */
        val source: String,

        /**
         *  The section of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”,
         *  “identity_card”, “internal_passport”
         */
        val type: String,

        /**
         *  Base64-encoded hash of the file with the selfie
         */
        val fileHash: String,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  Represents an issue with a document scan. The error is considered resolved when the file with the document scan
 *  changes.
 */
data class PassportElementErrorFile(

        /**
         *  Error source, must be file
         */
        val source: String,

        /**
         *  The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”,
         *  “rental_agreement”, “passport_registration”, “temporary_registration”
         */
        val type: String,

        /**
         *  Base64-encoded file hash
         */
        val fileHash: String,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  Represents an issue with a list of scans. The error is considered resolved when the list of files containing the
 *  scans changes.
 */
data class PassportElementErrorFiles(

        /**
         *  Error source, must be files
         */
        val source: String,

        /**
         *  The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”,
         *  “rental_agreement”, “passport_registration”, “temporary_registration”
         */
        val type: String,

        /**
         *  List of base64-encoded file hashes
         */
        val fileHashes: List<String>,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  Represents an issue with one of the files that constitute the translation of a document. The error is considered
 *  resolved when the file changes.
 */
data class PassportElementErrorTranslationFile(

        /**
         *  Error source, must be translation_file
         */
        val source: String,

        /**
         *  Type of element of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”,
         *  “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”,
         *  “passport_registration”, “temporary_registration”
         */
        val type: String,

        /**
         *  Base64-encoded file hash
         */
        val fileHash: String,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  Represents an issue with the translated version of a document. The error is considered resolved when a file with
 *  the document translation change.
 */
data class PassportElementErrorTranslationFiles(

        /**
         *  Error source, must be translation_files
         */
        val source: String,

        /**
         *  Type of element of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”,
         *  “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”,
         *  “passport_registration”, “temporary_registration”
         */
        val type: String,

        /**
         *  List of base64-encoded file hashes
         */
        val fileHashes: List<String>,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  Represents an issue in an unspecified place. The error is considered resolved when new data is added.
 */
data class PassportElementErrorUnspecified(

        /**
         *  Error source, must be unspecified
         */
        val source: String,

        /**
         *  Type of element of the user's Telegram Passport which has the issue
         */
        val type: String,

        /**
         *  Base64-encoded element hash
         */
        val elementHash: String,

        /**
         *  Error message
         */
        val message: String
)


/**
 *  This object represents a game. Use BotFather to create and edit games, their short names will act as unique
 *  identifiers.
 */
data class Game(

        /**
         *  Title of the game
         */
        val title: String,

        /**
         *  Description of the game
         */
        val description: String,

        /**
         *  Photo that will be displayed in the game message in chats.
         */
        val photo: List<PhotoSize>,

        /**
         *  Brief description of the game or high scores included in the game message. Can be automatically edited to
         *  include current high scores for the game when the bot calls setGameScore, or manually edited using
         *  editMessageText. 0-4096 characters.
         */
        val text: String? = null,

        /**
         *  Special entities that appear in text, such as usernames, URLs, bot commands, etc.
         */
        val textEntities: List<MessageEntity>? = null,

        /**
         *  Animation that will be displayed in the game message in chats. Upload via BotFather
         */
        val animation: Animation? = null
)


/**
 *  A placeholder, currently holds no information. Use BotFather to set up your game.
 */
class CallbackGame


/**
 *  This object represents one row of the high scores table for a game.
And that's about all we've got for now.If
 *  you've got any questions, please check out our Bot FAQ »
 */
data class GameHighScore(

        /**
         *  Position in high score table for the game
         */
        val position: Int,

        /**
         *  User
         */
        val user: User,

        /**
         *  Score
         */
        val score: Int
)


enum class ChatType(@JsonValue val jsonName: String) {
    PRIVATE("private"),
    GROUP("group"),
    SUPERGROUP("supergroup"),
    CHANNEL("channel");

    override fun toString() = jsonName
}



enum class ParseMode(@JsonValue val jsonName: String) {
    MARKDOWN_V2("MarkdownV2"),
    MARKDOWN("Markdown"),
    HTML("HTML");

    override fun toString() = jsonName
}



enum class ChatAction(@JsonValue val jsonName: String) {
    TYPING("typing"),
    UPLOAD_PHOTO("upload_photo"),
    UPLOAD_AUDIO("upload_audio"),
    UPLOAD_DOCUMENT("upload_document"),
    FIND_LOCATION("find_location"),
    RECORD_VIDEO_NOTE("record_video_note"),
    UPLOAD_VIDEO_NOTE("upload_video_note");

    override fun toString() = jsonName
}



interface Markup
