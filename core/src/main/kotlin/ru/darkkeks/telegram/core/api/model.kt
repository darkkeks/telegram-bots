@file:Suppress("unused")

package ru.darkkeks.telegram.core.api

import com.fasterxml.jackson.annotation.JsonValue


/**
 *  This object represents an incoming update.At most one of the optional parameters can be present in any given
 *  update.
 */
data class Update(

    /**
     *  The update's unique identifier. Update identifiers start from a certain positive number and increase
     *  sequentially. This ID becomes especially handy if you're using Webhooks, since it allows you to ignore repeated
     *  updates or to restore the correct update sequence, should they get out of order. If there are no new updates
     *  for at least a week, then identifier of the next update will be chosen randomly instead of sequentially.
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
     *  A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the
     *  bot itself.
     */
    val pollAnswer: PollAnswer? = null,

    /**
     *  The bot's chat member status was updated in a chat. For private chats, this update is received only when the
     *  bot is blocked or unblocked by the user.
     */
    val myChatMember: ChatMemberUpdated? = null,

    /**
     *  A chat member's status was updated in a chat. The bot must be an administrator in the chat and must explicitly
     *  specify “chat_member” in the list of allowed_updates to receive these updates.
     */
    val chatMember: ChatMemberUpdated? = null,

    /**
     *  A request to join the chat has been sent. The bot must have the can_invite_users administrator right in the
     *  chat to receive these updates.
     */
    val chatJoinRequest: ChatJoinRequest? = null
)


/**
 *  Use this method to receive incoming updates using long polling (wiki). An Array of Update objects is returned.
 */
data class GetUpdatesRequest(

    /**
     *  Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of
     *  previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An
     *  update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id. The
     *  negative offset can be specified to retrieve updates starting from -offset update from the end of the updates
     *  queue. All previous updates will forgotten.
     */
    val offset: Int? = null,

    /**
     *  Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     */
    val limit: Int? = null,

    /**
     *  Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling
     *  should be used for testing purposes only.
     */
    val timeout: Int? = null,

    /**
     *  A JSON-serialized list of the update types you want your bot to receive. For example, specify [“message”,
     *  “edited_channel_post”, “callback_query”] to only receive updates of these types. See Update for a complete list
     *  of available update types. Specify an empty list to receive all update types except chat_member (default). If
     *  not specified, the previous setting will be used.Please note that this parameter doesn't affect updates created
     *  before the call to the getUpdates, so unwanted updates may be received for a short period of time.
     */
    val allowedUpdates: List<String>? = null
)


/**
 *  Use this method to specify a url and receive incoming updates via an outgoing webhook. Whenever there is an update
 *  for the bot, we will send an HTTPS POST request to the specified url, containing a JSON-serialized Update. In case
 *  of an unsuccessful request, we will give up after a reasonable amount of attempts. Returns True on success.
 *
 *  If you'd like to make sure that the Webhook request comes from Telegram, we recommend using a secret path in the
 *  URL, e.g. https://www.example.com/<token>. Since nobody else knows your bot's token, you can be pretty sure it's
 *  us.
 */
data class SetWebhookRequest(

    /**
     *  HTTPS url to send updates to. Use an empty string to remove webhook integration
     */
    val url: String,

    /**
     *  Upload your public key certificate so that the root certificate in use can be checked. See our self-signed
     *  guide for details.
     */
    val certificate: InputFile? = null,

    /**
     *  The fixed IP address which will be used to send webhook requests instead of the IP address resolved through DNS
     */
    val ipAddress: String? = null,

    /**
     *  Maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100. Defaults to
     *  40. Use lower values to limit the load on your bot's server, and higher values to increase your bot's
     *  throughput.
     */
    val maxConnections: Int? = null,

    /**
     *  A JSON-serialized list of the update types you want your bot to receive. For example, specify [“message”,
     *  “edited_channel_post”, “callback_query”] to only receive updates of these types. See Update for a complete list
     *  of available update types. Specify an empty list to receive all update types except chat_member (default). If
     *  not specified, the previous setting will be used.Please note that this parameter doesn't affect updates created
     *  before the call to the setWebhook, so unwanted updates may be received for a short period of time.
     */
    val allowedUpdates: List<String>? = null,

    /**
     *  Pass True to drop all pending updates
     */
    val dropPendingUpdates: Boolean? = null
)


/**
 *  Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns True on success.
 */
data class DeleteWebhookRequest(

    /**
     *  Pass True to drop all pending updates
     */
    val dropPendingUpdates: Boolean? = null
)


/**
 *  Use this method to get current webhook status. Requires no parameters. On success, returns a WebhookInfo object. If
 *  the bot is using getUpdates, will return an object with the url field empty.
 */
class GetWebhookInfoRequest


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
     *  Currently used webhook IP address
     */
    val ipAddress: String? = null,

    /**
     *  Unix time for the most recent error that happened when trying to deliver an update via webhook
     */
    val lastErrorDate: Int? = null,

    /**
     *  Error message in human-readable format for the most recent error that happened when trying to deliver an update
     *  via webhook
     */
    val lastErrorMessage: String? = null,

    /**
     *  Maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery
     */
    val maxConnections: Int? = null,

    /**
     *  A list of update types the bot is subscribed to. Defaults to all update types except chat_member
     */
    val allowedUpdates: List<String>? = null
)


/**
 *  This object represents a Telegram user or bot.
 */
data class User(

    /**
     *  Unique identifier for this user or bot. This number may have more than 32 significant bits and some programming
     *  languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a
     *  64-bit integer or double-precision float type are safe for storing this identifier.
     */
    val id: Long,

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
     *  Unique identifier for this chat. This number may have more than 32 significant bits and some programming
     *  languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a
     *  signed 64-bit integer or double-precision float type are safe for storing this identifier.
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
     *  Bio of the other party in a private chat. Returned only in getChat.
     */
    val bio: String? = null,

    /**
     *  True, if privacy settings of the other party in the private chat allows to use tg://user?id=<user_id> links
     *  only in chats with the user. Returned only in getChat.
     */
    val hasPrivateForwards: Boolean? = null,

    /**
     *  Description, for groups, supergroups and channel chats. Returned only in getChat.
     */
    val description: String? = null,

    /**
     *  Primary invite link, for groups, supergroups and channel chats. Returned only in getChat.
     */
    val inviteLink: String? = null,

    /**
     *  The most recent pinned message (by sending date). Returned only in getChat.
     */
    val pinnedMessage: Message? = null,

    /**
     *  Default chat member permissions, for groups and supergroups. Returned only in getChat.
     */
    val permissions: ChatPermissions? = null,

    /**
     *  For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user; in
     *  seconds. Returned only in getChat.
     */
    val slowModeDelay: Int? = null,

    /**
     *  The time after which all messages sent to the chat will be automatically deleted; in seconds. Returned only in
     *  getChat.
     */
    val messageAutoDeleteTime: Int? = null,

    /**
     *  True, if messages from the chat can't be forwarded to other chats. Returned only in getChat.
     */
    val hasProtectedContent: Boolean? = null,

    /**
     *  For supergroups, name of group sticker set. Returned only in getChat.
     */
    val stickerSetName: String? = null,

    /**
     *  True, if the bot can change the group sticker set. Returned only in getChat.
     */
    val canSetStickerSet: Boolean? = null,

    /**
     *  Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for
     *  supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may
     *  have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer
     *  or double-precision float type are safe for storing this identifier. Returned only in getChat.
     */
    val linkedChatId: Long? = null,

    /**
     *  For supergroups, the location to which the supergroup is connected. Returned only in getChat.
     */
    val location: ChatLocation? = null
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
     *  Sender of the message; empty for messages sent to channels. For backward compatibility, the field contains a
     *  fake sender user in non-channel chats, if the message was sent on behalf of a chat.
     */
    val from: User? = null,

    /**
     *  Sender of the message, sent on behalf of a chat. For example, the channel itself for channel posts, the
     *  supergroup itself for messages from anonymous group administrators, the linked channel for messages
     *  automatically forwarded to the discussion group.  For backward compatibility, the field from contains a fake
     *  sender user in non-channel chats, if the message was sent on behalf of a chat.
     */
    val senderChat: Chat? = null,

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
     *  For messages forwarded from channels or from anonymous administrators, information about the original sender
     *  chat
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
     *  True, if the message is a channel post that was automatically forwarded to the connected discussion group
     */
    val isAutomaticForward: Boolean? = null,

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
     *  True, if the message can't be forwarded
     */
    val hasProtectedContent: Boolean? = null,

    /**
     *  The unique identifier of a media message group this message belongs to
     */
    val mediaGroupId: String? = null,

    /**
     *  Signature of the post author for messages in channels, or the custom title of an anonymous group administrator
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
     *  Message is an animation, information about the animation. For backward compatibility, when this field is set,
     *  the document field will also be set
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
     *  Message is a dice with random value
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
     *  New members that were added to the group or supergroup and information about them (the bot itself may be one of
     *  these members)
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
     *  Service message: auto-delete timer settings changed in the chat
     */
    val messageAutoDeleteTimerChanged: MessageAutoDeleteTimerChanged? = null,

    /**
     *  The group has been migrated to a supergroup with the specified identifier. This number may have more than 32
     *  significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it
     *  has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing
     *  this identifier.
     */
    val migrateToChatId: Long? = null,

    /**
     *  The supergroup has been migrated from a group with the specified identifier. This number may have more than 32
     *  significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it
     *  has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing
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
     *  Message is a service message about a successful payment, information about the payment. More about payments »
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
     *  Service message. A user in the chat triggered another user's proximity alert while sharing Live Location.
     */
    val proximityAlertTriggered: ProximityAlertTriggered? = null,

    /**
     *  Service message: voice chat scheduled
     */
    val voiceChatScheduled: VoiceChatScheduled? = null,

    /**
     *  Service message: voice chat started
     */
    val voiceChatStarted: VoiceChatStarted? = null,

    /**
     *  Service message: voice chat ended
     */
    val voiceChatEnded: VoiceChatEnded? = null,

    /**
     *  Service message: new participants invited to a voice chat
     */
    val voiceChatParticipantsInvited: VoiceChatParticipantsInvited? = null,

    /**
     *  Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  This object represents a unique message identifier.
 */
data class MessageId(

    /**
     *  Unique message identifier
     */
    val messageId: Int
)


/**
 *  This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 */
data class MessageEntity(

    /**
     *  Type of the entity. Can be “mention” (@username), “hashtag” (#hashtag), “cashtag” ($USD), “bot_command”
     *  (/start@jobs_bot), “url” (https://telegram.org), “email” (do-not-reply@telegram.org), “phone_number”
     *  (+1-212-555-0123), “bold” (bold text), “italic” (italic text), “underline” (underlined text), “strikethrough”
     *  (strikethrough text), “code” (monowidth string), “pre” (monowidth block), “text_link” (for clickable text
     *  URLs), “text_mention” (for users without usernames)
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
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
     *  File size in bytes
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
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
     *  File size in bytes
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
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
     *  Original filename as defined by sender
     */
    val fileName: String? = null,

    /**
     *  MIME type of the file as defined by sender
     */
    val mimeType: String? = null,

    /**
     *  File size in bytes
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
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
     *  File size in bytes
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
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
     *  Original filename as defined by sender
     */
    val fileName: String? = null,

    /**
     *  Mime type of a file as defined by sender
     */
    val mimeType: String? = null,

    /**
     *  File size in bytes
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
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
     *  File size in bytes
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
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
     *  File size in bytes
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
     *  Contact's user identifier in Telegram. This number may have more than 32 significant bits and some programming
     *  languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a
     *  64-bit integer or double-precision float type are safe for storing this identifier.
     */
    val userId: Long? = null,

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
     *  Value of the dice, 1-6 for “”, “” and “” base emoji, 1-5 for “” and “” base emoji, 1-64 for “” base emoji
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
     *  Poll question, 1-300 characters
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
     *  0-based identifier of the correct answer option. Available only for polls in the quiz mode, which are closed,
     *  or was sent (not forwarded) by the bot or to the private chat with the bot.
     */
    val correctOptionId: Int? = null,

    /**
     *  Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200
     *  characters
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
    val latitude: Float,

    /**
     *  The radius of uncertainty for the location, measured in meters; 0-1500
     */
    val horizontalAccuracy: Double? = null,

    /**
     *  Time relative to the message sending date, during which the location can be updated; in seconds. For active
     *  live locations only.
     */
    val livePeriod: Int? = null,

    /**
     *  The direction in which user is moving, in degrees; 1-360. For active live locations only.
     */
    val heading: Int? = null,

    /**
     *  Maximum distance for proximity alerts about approaching another chat member, in meters. For sent live locations
     *  only.
     */
    val proximityAlertRadius: Int? = null
)


/**
 *  This object represents a venue.
 */
data class Venue(

    /**
     *  Venue location. Can't be a live location
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
    val foursquareType: String? = null,

    /**
     *  Google Places identifier of the venue
     */
    val googlePlaceId: String? = null,

    /**
     *  Google Places type of the venue. (See supported types.)
     */
    val googlePlaceType: String? = null
)


/**
 *  This object represents the content of a service message, sent whenever a user in the chat triggers a proximity
 *  alert set by another user.
 */
data class ProximityAlertTriggered(

    /**
     *  User that triggered the alert
     */
    val traveler: User,

    /**
     *  User that set the alert
     */
    val watcher: User,

    /**
     *  The distance between the users
     */
    val distance: Int
)


/**
 *  This object represents a service message about a change in auto-delete timer settings.
 */
data class MessageAutoDeleteTimerChanged(

    /**
     *  New auto-delete time for messages in the chat; in seconds
     */
    val messageAutoDeleteTime: Int
)


/**
 *  This object represents a service message about a voice chat scheduled in the chat.
 */
data class VoiceChatScheduled(

    /**
     *  Point in time (Unix timestamp) when the voice chat is supposed to be started by a chat administrator
     */
    val startDate: Int
)


/**
 *  This object represents a service message about a voice chat started in the chat. Currently holds no information.
 */
class VoiceChatStarted


/**
 *  This object represents a service message about a voice chat ended in the chat.
 */
data class VoiceChatEnded(

    /**
     *  Voice chat duration in seconds
     */
    val duration: Int
)


/**
 *  This object represents a service message about new members invited to a voice chat.
 */
data class VoiceChatParticipantsInvited(

    /**
     *  New members that were invited to the voice chat
     */
    val users: List<User>? = null
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
     */
    val fileUniqueId: String,

    /**
     *  File size in bytes, if known
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
     *  Requests clients to resize the keyboard vertically for optimal fit (e.g., make the keyboard smaller if there
     *  are just two rows of buttons). Defaults to false, in which case the custom keyboard is always of the same
     *  height as the app's standard keyboard.
     */
    val resizeKeyboard: Boolean? = null,

    /**
     *  Requests clients to hide the keyboard as soon as it's been used. The keyboard will still be available, but
     *  clients will automatically display the usual letter-keyboard in the chat – the user can press a special button
     *  in the input field to see the custom keyboard again. Defaults to false.
     */
    val oneTimeKeyboard: Boolean? = null,

    /**
     *  The placeholder to be shown in the input field when the keyboard is active; 1-64 characters
     */
    val inputFieldPlaceholder: String? = null,

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
 *
 *  Note: request_contact and request_location options will only work in Telegram versions released after 9 April,
 *  2016. Older clients will display unsupported message.Note: request_poll option will only work in Telegram versions
 *  released after 23 January, 2020. Older clients will display unsupported message.
 */
data class KeyboardButton(

    /**
     *  Text of the button. If none of the optional fields are used, it will be sent as a message when the button is
     *  pressed
     */
    val text: String,

    /**
     *  If True, the user's phone number will be sent as a contact when the button is pressed. Available in private
     *  chats only
     */
    val requestContact: Boolean? = null,

    /**
     *  If True, the user's current location will be sent when the button is pressed. Available in private chats only
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
     *  If quiz is passed, the user will be allowed to create only polls in the quiz mode. If regular is passed, only
     *  regular polls will be allowed. Otherwise, the user will be allowed to create a poll of any type.
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
     *  Requests clients to remove the custom keyboard (user will not be able to summon this keyboard; if you want to
     *  hide the keyboard from sight but keep it accessible, use one_time_keyboard in ReplyKeyboardMarkup)
     */
    val removeKeyboard: Boolean,

    /**
     *  Use this parameter if you want to remove the keyboard for specific users only. Targets: 1) users that are
     *  @mentioned in the text of the Message object; 2) if the bot's message is a reply (has reply_to_message_id),
     *  sender of the original message.Example: A user votes in a poll, bot returns confirmation message in reply to
     *  the vote and removes the keyboard for that user, while still showing the keyboard with poll options to users
     *  who haven't voted yet.
     */
    val selective: Boolean? = null
) : Markup


/**
 *  This object represents an inline keyboard that appears right next to the message it belongs to.
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will display unsupported
 *  message.
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
     *  HTTP or tg:// url to be opened when the button is pressed. Links tg://user?id=<user_id> can be used to mention
     *  a user by their ID without using a username, if this is allowed by their privacy settings.
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
     *  If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the
     *  bot's username and the specified inline query in the input field. Can be empty, in which case just the bot's
     *  username will be inserted.Note: This offers an easy way for users to start using your bot in inline mode when
     *  they are currently in a private chat with it. Especially useful when combined with switch_pm… actions – in this
     *  case the user will be automatically returned to the chat they switched from, skipping the chat selection
     *  screen.
     */
    val switchInlineQuery: String? = null,

    /**
     *  If set, pressing the button will insert the bot's username and the specified inline query in the current chat's
     *  input field. Can be empty, in which case only the bot's username will be inserted.This offers a quick way for
     *  the user to open your bot in inline mode in the same chat – good for selecting something from multiple options.
     */
    val switchInlineQueryCurrentChat: String? = null,

    /**
     *  Description of the game that will be launched when the user presses the button.NOTE: This type of button must
     *  always be the first button in the first row.
     */
    val callbackGame: CallbackGame? = null,

    /**
     *  Specify True, to send a Pay button.NOTE: This type of button must always be the first button in the first row
     *  and can only be used in invoice messages.
     */
    val pay: Boolean? = null
)


/**
 *  This object represents a parameter of the inline keyboard button used to automatically authorize a user. Serves as
 *  a great replacement for the Telegram Login Widget when the user is coming from Telegram. All the user needs to do
 *  is tap/click a button and confirm that they want to log in:
 *
 *  Telegram apps support these buttons as of version 5.7.
 */
data class LoginUrl(

    /**
     *  An HTTP URL to be opened with user authorization data added to the query string when the button is pressed. If
     *  the user refuses to provide authorization data, the original URL without information about the user will be
     *  opened. The data added is the same as described in Receiving authorization data.NOTE: You must always check the
     *  hash of the received data to verify the authentication and the integrity of the data as described in Checking
     *  authorization.
     */
    val url: String,

    /**
     *  New text of the button in forwarded messages.
     */
    val forwardText: String? = null,

    /**
     *  Username of a bot, which will be used for user authorization. See Setting up a bot for more details. If not
     *  specified, the current bot's username will be assumed. The url's domain must be the same as the domain linked
     *  with the bot. See Linking your domain to the bot for more details.
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
     *  Message with the callback button that originated the query. Note that message content and message date will not
     *  be available if the message is too old
     */
    val message: Message? = null,

    /**
     *  Identifier of the message sent via the bot in inline mode, that originated the query.
     */
    val inlineMessageId: String? = null,

    /**
     *  Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent.
     *  Useful for high scores in games.
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
     *  The placeholder to be shown in the input field when the reply is active; 1-64 characters
     */
    val inputFieldPlaceholder: String? = null,

    /**
     *  Use this parameter if you want to force reply from specific users only. Targets: 1) users that are @mentioned
     *  in the text of the Message object; 2) if the bot's message is a reply (has reply_to_message_id), sender of the
     *  original message.
     */
    val selective: Boolean? = null
) : Markup


/**
 *  This object represents a chat photo.
 */
data class ChatPhoto(

    /**
     *  File identifier of small (160x160) chat photo. This file_id can be used only for photo download and only for as
     *  long as the photo is not changed.
     */
    val smallFileId: String,

    /**
     *  Unique file identifier of small (160x160) chat photo, which is supposed to be the same over time and for
     *  different bots. Can't be used to download or reuse the file.
     */
    val smallFileUniqueId: String,

    /**
     *  File identifier of big (640x640) chat photo. This file_id can be used only for photo download and only for as
     *  long as the photo is not changed.
     */
    val bigFileId: String,

    /**
     *  Unique file identifier of big (640x640) chat photo, which is supposed to be the same over time and for
     *  different bots. Can't be used to download or reuse the file.
     */
    val bigFileUniqueId: String
)


/**
 *  Represents an invite link for a chat.
 */
data class ChatInviteLink(

    /**
     *  The invite link. If the link was created by another chat administrator, then the second part of the link will
     *  be replaced with “…”.
     */
    val inviteLink: String,

    /**
     *  Creator of the link
     */
    val creator: User,

    /**
     *  True, if users joining the chat via the link need to be approved by chat administrators
     */
    val createsJoinRequest: Boolean,

    /**
     *  True, if the link is primary
     */
    val isPrimary: Boolean,

    /**
     *  True, if the link is revoked
     */
    val isRevoked: Boolean,

    /**
     *  Invite link name
     */
    val name: String? = null,

    /**
     *  Point in time (Unix timestamp) when the link will expire or has been expired
     */
    val expireDate: Int? = null,

    /**
     *  Maximum number of users that can be members of the chat simultaneously after joining the chat via this invite
     *  link; 1-99999
     */
    val memberLimit: Int? = null,

    /**
     *  Number of pending join requests created using this link
     */
    val pendingJoinRequestCount: Int? = null
)


/**
 *  This object contains information about one member of a chat. Currently, the following 6 types of chat members are
 *  supported:
 */
class ChatMember


/**
 *  Represents a chat member that owns the chat and has all administrator privileges.
 */
data class ChatMemberOwner(

    /**
     *  The member's status in the chat, always “creator”
     */
    val status: String,

    /**
     *  Information about the user
     */
    val user: User,

    /**
     *  True, if the user's presence in the chat is hidden
     */
    val isAnonymous: Boolean,

    /**
     *  Custom title for this user
     */
    val customTitle: String? = null
)


/**
 *  Represents a chat member that has some additional privileges.
 */
data class ChatMemberAdministrator(

    /**
     *  The member's status in the chat, always “administrator”
     */
    val status: String,

    /**
     *  Information about the user
     */
    val user: User,

    /**
     *  True, if the bot is allowed to edit administrator privileges of that user
     */
    val canBeEdited: Boolean,

    /**
     *  True, if the user's presence in the chat is hidden
     */
    val isAnonymous: Boolean,

    /**
     *  True, if the administrator can access the chat event log, chat statistics, message statistics in channels, see
     *  channel members, see anonymous administrators in supergroups and ignore slow mode. Implied by any other
     *  administrator privilege
     */
    val canManageChat: Boolean,

    /**
     *  True, if the administrator can delete messages of other users
     */
    val canDeleteMessages: Boolean,

    /**
     *  True, if the administrator can manage voice chats
     */
    val canManageVoiceChats: Boolean,

    /**
     *  True, if the administrator can restrict, ban or unban chat members
     */
    val canRestrictMembers: Boolean,

    /**
     *  True, if the administrator can add new administrators with a subset of their own privileges or demote
     *  administrators that he has promoted, directly or indirectly (promoted by administrators that were appointed by
     *  the user)
     */
    val canPromoteMembers: Boolean,

    /**
     *  True, if the user is allowed to change the chat title, photo and other settings
     */
    val canChangeInfo: Boolean,

    /**
     *  True, if the user is allowed to invite new users to the chat
     */
    val canInviteUsers: Boolean,

    /**
     *  True, if the administrator can post in the channel; channels only
     */
    val canPostMessages: Boolean? = null,

    /**
     *  True, if the administrator can edit messages of other users and can pin messages; channels only
     */
    val canEditMessages: Boolean? = null,

    /**
     *  True, if the user is allowed to pin messages; groups and supergroups only
     */
    val canPinMessages: Boolean? = null,

    /**
     *  Custom title for this user
     */
    val customTitle: String? = null
)


/**
 *  Represents a chat member that has no additional privileges or restrictions.
 */
data class ChatMemberMember(

    /**
     *  The member's status in the chat, always “member”
     */
    val status: String,

    /**
     *  Information about the user
     */
    val user: User
)


/**
 *  Represents a chat member that is under certain restrictions in the chat. Supergroups only.
 */
data class ChatMemberRestricted(

    /**
     *  The member's status in the chat, always “restricted”
     */
    val status: String,

    /**
     *  Information about the user
     */
    val user: User,

    /**
     *  True, if the user is a member of the chat at the moment of the request
     */
    val isMember: Boolean,

    /**
     *  True, if the user is allowed to change the chat title, photo and other settings
     */
    val canChangeInfo: Boolean,

    /**
     *  True, if the user is allowed to invite new users to the chat
     */
    val canInviteUsers: Boolean,

    /**
     *  True, if the user is allowed to pin messages
     */
    val canPinMessages: Boolean,

    /**
     *  True, if the user is allowed to send text messages, contacts, locations and venues
     */
    val canSendMessages: Boolean,

    /**
     *  True, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes
     */
    val canSendMediaMessages: Boolean,

    /**
     *  True, if the user is allowed to send polls
     */
    val canSendPolls: Boolean,

    /**
     *  True, if the user is allowed to send animations, games, stickers and use inline bots
     */
    val canSendOtherMessages: Boolean,

    /**
     *  True, if the user is allowed to add web page previews to their messages
     */
    val canAddWebPagePreviews: Boolean,

    /**
     *  Date when restrictions will be lifted for this user; unix time. If 0, then the user is restricted forever
     */
    val untilDate: Int
)


/**
 *  Represents a chat member that isn't currently a member of the chat, but may join it themselves.
 */
data class ChatMemberLeft(

    /**
     *  The member's status in the chat, always “left”
     */
    val status: String,

    /**
     *  Information about the user
     */
    val user: User
)


/**
 *  Represents a chat member that was banned in the chat and can't return to the chat or view chat messages.
 */
data class ChatMemberBanned(

    /**
     *  The member's status in the chat, always “kicked”
     */
    val status: String,

    /**
     *  Information about the user
     */
    val user: User,

    /**
     *  Date when restrictions will be lifted for this user; unix time. If 0, then the user is banned forever
     */
    val untilDate: Int
)


/**
 *  This object represents changes in the status of a chat member.
 */
data class ChatMemberUpdated(

    /**
     *  Chat the user belongs to
     */
    val chat: Chat,

    /**
     *  Performer of the action, which resulted in the change
     */
    val from: User,

    /**
     *  Date the change was done in Unix time
     */
    val date: Int,

    /**
     *  Previous information about the chat member
     */
    val oldChatMember: ChatMember,

    /**
     *  New information about the chat member
     */
    val newChatMember: ChatMember,

    /**
     *  Chat invite link, which was used by the user to join the chat; for joining by invite link events only.
     */
    val inviteLink: ChatInviteLink? = null
)


/**
 *  Represents a join request sent to a chat.
 */
data class ChatJoinRequest(

    /**
     *  Chat to which the request was sent
     */
    val chat: Chat,

    /**
     *  User that sent the join request
     */
    val from: User,

    /**
     *  Date the request was sent in Unix time
     */
    val date: Int,

    /**
     *  Bio of the user.
     */
    val bio: String? = null,

    /**
     *  Chat invite link that was used by the user to send the join request
     */
    val inviteLink: ChatInviteLink? = null
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
     *  True, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes, implies
     *  can_send_messages
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
     *  True, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups
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
 *  Represents a location to which a chat is connected.
 */
data class ChatLocation(

    /**
     *  The location to which the supergroup is connected. Can't be a live location.
     */
    val location: Location,

    /**
     *  Location address; 1-64 characters, as defined by the chat owner
     */
    val address: String
)


/**
 *  This object represents a bot command.
 */
data class BotCommand(

    /**
     *  Text of the command; 1-32 characters. Can contain only lowercase English letters, digits and underscores.
     */
    val command: String,

    /**
     *  Description of the command; 1-256 characters.
     */
    val description: String
)


/**
 *  This object represents the scope to which bot commands are applied. Currently, the following 7 scopes are
 *  supported:
 */
class BotCommandScope


/**
 *  Represents the default scope of bot commands. Default commands are used if no commands with a narrower scope are
 *  specified for the user.
 */
data class BotCommandScopeDefault(

    /**
     *  Scope type, must be default
     */
    val type: String
)


/**
 *  Represents the scope of bot commands, covering all private chats.
 */
data class BotCommandScopeAllPrivateChats(

    /**
     *  Scope type, must be all_private_chats
     */
    val type: String
)


/**
 *  Represents the scope of bot commands, covering all group and supergroup chats.
 */
data class BotCommandScopeAllGroupChats(

    /**
     *  Scope type, must be all_group_chats
     */
    val type: String
)


/**
 *  Represents the scope of bot commands, covering all group and supergroup chat administrators.
 */
data class BotCommandScopeAllChatAdministrators(

    /**
     *  Scope type, must be all_chat_administrators
     */
    val type: String
)


/**
 *  Represents the scope of bot commands, covering a specific chat.
 */
data class BotCommandScopeChat(

    /**
     *  Scope type, must be chat
     */
    val type: String,

    /**
     *  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    val chatId: Long
)


/**
 *  Represents the scope of bot commands, covering all administrators of a specific group or supergroup chat.
 */
data class BotCommandScopeChatAdministrators(

    /**
     *  Scope type, must be chat_administrators
     */
    val type: String,

    /**
     *  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    val chatId: Long
)


/**
 *  Represents the scope of bot commands, covering a specific member of a group or supergroup chat.
 */
data class BotCommandScopeChatMember(

    /**
     *  Scope type, must be chat_member
     */
    val type: String,

    /**
     *  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int
)


/**
 *  Contains information about why a request was unsuccessful.
 */
data class ResponseParameters(

    /**
     *  The group has been migrated to a supergroup with the specified identifier. This number may have more than 32
     *  significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it
     *  has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing
     *  this identifier.
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
     *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL
     *  for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using
     *  multipart/form-data under <file_attach_name> name. More info on Sending Files »
     */
    val media: String,

    /**
     *  Caption of the photo to be sent, 0-1024 characters after entities parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the photo caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null
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
     *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL
     *  for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using
     *  multipart/form-data under <file_attach_name> name. More info on Sending Files »
     */
    val media: String,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Video width
     */
    val width: Int? = null,

    /**
     *  Video height
     */
    val height: Int? = null,

    /**
     *  Video duration in seconds
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
     *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL
     *  for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using
     *  multipart/form-data under <file_attach_name> name. More info on Sending Files »
     */
    val media: String,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Animation width
     */
    val width: Int? = null,

    /**
     *  Animation height
     */
    val height: Int? = null,

    /**
     *  Animation duration in seconds
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
     *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL
     *  for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using
     *  multipart/form-data under <file_attach_name> name. More info on Sending Files »
     */
    val media: String,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
     *  File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL
     *  for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using
     *  multipart/form-data under <file_attach_name> name. More info on Sending Files »
     */
    val media: String,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
     */
    val thumb: InputFile? = null,

    /**
     *  Caption of the document to be sent, 0-1024 characters after entities parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the document caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Disables automatic server-side content type detection for files uploaded using multipart/form-data. Always
     *  True, if the document is sent as part of an album.
     */
    val disableContentTypeDetection: Boolean? = null
) : InputMedia


/**
 *  This object represents the contents of a file to be uploaded. Must be posted using multipart/form-data in the usual
 *  way that files are uploaded via the browser.
 */
class InputFile


/**
 *  A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information
 *  about the bot in form of a User object.
 */
class GetMeRequest


/**
 *  Use this method to log out from the cloud Bot API server before launching the bot locally. You must log out the bot
 *  before running it locally, otherwise there is no guarantee that the bot will receive updates. After a successful
 *  call, you can immediately log in on a local server, but will not be able to log in back to the cloud Bot API server
 *  for 10 minutes. Returns True on success. Requires no parameters.
 */
class LogOutRequest


/**
 *  Use this method to close the bot instance before moving it from one local server to another. You need to delete the
 *  webhook before calling this method to ensure that the bot isn't launched again after server restart. The method
 *  will return error 429 in the first 10 minutes after the bot is launched. Returns True on success. Requires no
 *  parameters.
 */
class CloseRequest


/**
 *  Use this method to send text messages. On success, the sent Message is returned.
 */
data class SendMessageRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Text of the message to be sent, 1-4096 characters after entities parsing
     */
    val text: String,

    /**
     *  Mode for parsing entities in the message text. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in message text, which can be specified instead of
     *  parse_mode
     */
    val entities: List<MessageEntity>? = null,

    /**
     *  Disables link previews for links in this message
     */
    val disableWebPagePreview: Boolean? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to forward messages of any kind. Service messages can't be forwarded. On success, the sent Message
 *  is returned.
 */
data class ForwardMessageRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier for the chat where the original message was sent (or channel username in the format
     *  @channelusername)
     */
    val fromChatId: Long,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  Message identifier in the chat specified in from_chat_id
     */
    val messageId: Int
)


/**
 *  Use this method to copy messages of any kind. Service messages and invoice messages can't be copied. The method is
 *  analogous to the method forwardMessage, but the copied message doesn't have a link to the original message. Returns
 *  the MessageId of the sent message on success.
 */
data class CopyMessageRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier for the chat where the original message was sent (or channel username in the format
     *  @channelusername)
     */
    val fromChatId: Long,

    /**
     *  Message identifier in the chat specified in from_chat_id
     */
    val messageId: Int,

    /**
     *  New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the new caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the new caption, which can be specified instead of
     *  parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send photos. On success, the sent Message is returned.
 */
data class SendPhotoRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass
     *  an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using
     *  multipart/form-data. The photo must be at most 10 MB in size. The photo's width and height must not exceed
     *  10000 in total. Width and height ratio must be at most 20. More info on Sending Files »
     */
    val photo: InputFile,

    /**
     *  Photo caption (may also be used when resending photos by file_id), 0-1024 characters after entities parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the photo caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
     *  parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send audio files, if you want Telegram clients to display them in the music player. Your audio
 *  must be in the .MP3 or .M4A format. On success, the sent Message is returned. Bots can currently send audio files
 *  of up to 50 MB in size, this limit may be changed in the future.
 *
 *  For sending voice messages, use the sendVoice method instead.
 */
data class SendAudioRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Audio file to send. Pass a file_id as String to send an audio file that exists on the Telegram servers
     *  (recommended), pass an HTTP URL as a String for Telegram to get an audio file from the Internet, or upload a
     *  new one using multipart/form-data. More info on Sending Files »
     */
    val audio: InputFile,

    /**
     *  Audio caption, 0-1024 characters after entities parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the audio caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
     *  parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Duration of the audio in seconds
     */
    val duration: Int? = null,

    /**
     *  Performer
     */
    val performer: String? = null,

    /**
     *  Track name
     */
    val title: String? = null,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
     */
    val thumb: InputFile? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send general files. On success, the sent Message is returned. Bots can currently send files of
 *  any type of up to 50 MB in size, this limit may be changed in the future.
 */
data class SendDocumentRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  File to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass
     *  an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using
     *  multipart/form-data. More info on Sending Files »
     */
    val document: InputFile,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
     */
    val thumb: InputFile? = null,

    /**
     *  Document caption (may also be used when resending documents by file_id), 0-1024 characters after entities
     *  parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the document caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
     *  parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Disables automatic server-side content type detection for files uploaded using multipart/form-data
     */
    val disableContentTypeDetection: Boolean? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send video files, Telegram clients support mp4 videos (other formats may be sent as Document).
 *  On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size, this limit
 *  may be changed in the future.
 */
data class SendVideoRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended), pass
     *  an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using
     *  multipart/form-data. More info on Sending Files »
     */
    val video: InputFile,

    /**
     *  Duration of sent video in seconds
     */
    val duration: Int? = null,

    /**
     *  Video width
     */
    val width: Int? = null,

    /**
     *  Video height
     */
    val height: Int? = null,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
     */
    val thumb: InputFile? = null,

    /**
     *  Video caption (may also be used when resending videos by file_id), 0-1024 characters after entities parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the video caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
     *  parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Pass True, if the uploaded video is suitable for streaming
     */
    val supportsStreaming: Boolean? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent Message
 *  is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the
 *  future.
 */
data class SendAnimationRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers
     *  (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new
     *  animation using multipart/form-data. More info on Sending Files »
     */
    val animation: InputFile,

    /**
     *  Duration of sent animation in seconds
     */
    val duration: Int? = null,

    /**
     *  Animation width
     */
    val width: Int? = null,

    /**
     *  Animation height
     */
    val height: Int? = null,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
     */
    val thumb: InputFile? = null,

    /**
     *  Animation caption (may also be used when resending animation by file_id), 0-1024 characters after entities
     *  parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the animation caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
     *  parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message.
 *  For this to work, your audio must be in an .OGG file encoded with OPUS (other formats may be sent as Audio or
 *  Document). On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size,
 *  this limit may be changed in the future.
 */
data class SendVoiceRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Audio file to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended),
     *  pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using
     *  multipart/form-data. More info on Sending Files »
     */
    val voice: InputFile,

    /**
     *  Voice message caption, 0-1024 characters after entities parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the voice message caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
     *  parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  Duration of the voice message in seconds
     */
    val duration: Int? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  As of v.4.0, Telegram clients support rounded square mp4 videos of up to 1 minute long. Use this method to send
 *  video messages. On success, the sent Message is returned.
 */
data class SendVideoNoteRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Video note to send. Pass a file_id as String to send a video note that exists on the Telegram servers
     *  (recommended) or upload a new video using multipart/form-data. More info on Sending Files ». Sending video
     *  notes by a URL is currently unsupported
     */
    val videoNote: InputFile,

    /**
     *  Duration of sent video in seconds
     */
    val duration: Int? = null,

    /**
     *  Video width and height, i.e. diameter of the video message
     */
    val length: Int? = null,

    /**
     *  Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
     *  thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
     *  exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can
     *  be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded
     *  using multipart/form-data under <file_attach_name>. More info on Sending Files »
     */
    val thumb: InputFile? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can
 *  be only grouped in an album with messages of the same type. On success, an array of Messages that were sent is
 *  returned.
 */
data class SendMediaGroupRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  A JSON-serialized array describing messages to be sent, must include 2-10 items
     */
    val media: List<InputMedia>,

    /**
     *  Sends messages silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the messages are a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null
)


/**
 *  Use this method to send point on the map. On success, the sent Message is returned.
 */
data class SendLocationRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Latitude of the location
     */
    val latitude: Double,

    /**
     *  Longitude of the location
     */
    val longitude: Double,

    /**
     *  The radius of uncertainty for the location, measured in meters; 0-1500
     */
    val horizontalAccuracy: Double? = null,

    /**
     *  Period in seconds for which the location will be updated (see Live Locations, should be between 60 and 86400.
     */
    val livePeriod: Int? = null,

    /**
     *  For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if
     *  specified.
     */
    val heading: Int? = null,

    /**
     *  For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters.
     *  Must be between 1 and 100000 if specified.
     */
    val proximityAlertRadius: Int? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to edit live location messages. A location can be edited until its live_period expires or editing
 *  is explicitly disabled by a call to stopMessageLiveLocation. On success, if the edited message is not an inline
 *  message, the edited Message is returned, otherwise True is returned.
 */
data class EditMessageLiveLocationRequest(

    /**
     *  Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
     *  channel (in the format @channelusername)
     */
    val chatId: Long? = null,

    /**
     *  Required if inline_message_id is not specified. Identifier of the message to edit
     */
    val messageId: Int? = null,

    /**
     *  Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    val inlineMessageId: String? = null,

    /**
     *  Latitude of new location
     */
    val latitude: Double,

    /**
     *  Longitude of new location
     */
    val longitude: Double,

    /**
     *  The radius of uncertainty for the location, measured in meters; 0-1500
     */
    val horizontalAccuracy: Double? = null,

    /**
     *  Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     */
    val heading: Int? = null,

    /**
     *  Maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and
     *  100000 if specified.
     */
    val proximityAlertRadius: Int? = null,

    /**
     *  A JSON-serialized object for a new inline keyboard.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  Use this method to stop updating a live location message before live_period expires. On success, if the message is
 *  not an inline message, the edited Message is returned, otherwise True is returned.
 */
data class StopMessageLiveLocationRequest(

    /**
     *  Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
     *  channel (in the format @channelusername)
     */
    val chatId: Long? = null,

    /**
     *  Required if inline_message_id is not specified. Identifier of the message with live location to stop
     */
    val messageId: Int? = null,

    /**
     *  Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    val inlineMessageId: String? = null,

    /**
     *  A JSON-serialized object for a new inline keyboard.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  Use this method to send information about a venue. On success, the sent Message is returned.
 */
data class SendVenueRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Latitude of the venue
     */
    val latitude: Double,

    /**
     *  Longitude of the venue
     */
    val longitude: Double,

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
     *  Foursquare type of the venue, if known. (For example, “arts_entertainment/default”,
     *  “arts_entertainment/aquarium” or “food/icecream”.)
     */
    val foursquareType: String? = null,

    /**
     *  Google Places identifier of the venue
     */
    val googlePlaceId: String? = null,

    /**
     *  Google Places type of the venue. (See supported types.)
     */
    val googlePlaceType: String? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send phone contacts. On success, the sent Message is returned.
 */
data class SendContactRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

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
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send a native poll. On success, the sent Message is returned.
 */
data class SendPollRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Poll question, 1-300 characters
     */
    val question: String,

    /**
     *  A JSON-serialized list of answer options, 2-10 strings 1-100 characters each
     */
    val options: List<String>,

    /**
     *  True, if the poll needs to be anonymous, defaults to True
     */
    val isAnonymous: Boolean? = null,

    /**
     *  Poll type, “quiz” or “regular”, defaults to “regular”
     */
    val type: String? = null,

    /**
     *  True, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to False
     */
    val allowsMultipleAnswers: Boolean? = null,

    /**
     *  0-based identifier of the correct answer option, required for polls in quiz mode
     */
    val correctOptionId: Int? = null,

    /**
     *  Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200
     *  characters with at most 2 line feeds after entities parsing
     */
    val explanation: String? = null,

    /**
     *  Mode for parsing entities in the explanation. See formatting options for more details.
     */
    val explanationParseMode: String? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the poll explanation, which can be specified instead
     *  of parse_mode
     */
    val explanationEntities: List<MessageEntity>? = null,

    /**
     *  Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with
     *  close_date.
     */
    val openPeriod: Int? = null,

    /**
     *  Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than
     *  600 seconds in the future. Can't be used together with open_period.
     */
    val closeDate: Int? = null,

    /**
     *  Pass True, if the poll needs to be immediately closed. This can be useful for poll preview.
     */
    val isClosed: Boolean? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to send an animated emoji that will display a random value. On success, the sent Message is
 *  returned.
 */
data class SendDiceRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Emoji on which the dice throw animation is based. Currently, must be one of “”, “”, “”, “”, “”, or “”. Dice can
     *  have values 1-6 for “”, “” and “”, values 1-5 for “” and “”, and values 1-64 for “”. Defaults to “”
     */
    val emoji: String? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method when you need to tell the user that something is happening on the bot's side. The status is set for
 *  5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns True on
 *  success.
 *
 *  We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive.
 */
data class SendChatActionRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Type of action to broadcast. Choose one, depending on what the user is about to receive: typing for text
     *  messages, upload_photo for photos, record_video or upload_video for videos, record_voice or upload_voice for
     *  voice notes, upload_document for general files, choose_sticker for stickers, find_location for location data,
     *  record_video_note or upload_video_note for video notes.
     */
    val action: ChatAction
)


/**
 *  Use this method to get a list of profile pictures for a user. Returns a UserProfilePhotos object.
 */
data class GetUserProfilePhotosRequest(

    /**
     *  Unique identifier of the target user
     */
    val userId: Int,

    /**
     *  Sequential number of the first photo to be returned. By default, all photos are returned.
     */
    val offset: Int? = null,

    /**
     *  Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     */
    val limit: Int? = null
)


/**
 *  Use this method to get basic info about a file and prepare it for downloading. For the moment, bots can download
 *  files of up to 20MB in size. On success, a File object is returned. The file can then be downloaded via the link
 *  https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response. It is
 *  guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by
 *  calling getFile again.
 *
 *  Note: This function may not preserve the original file name and MIME type. You should save the file's MIME type and
 *  name (if available) when the File object is received.
 */
data class GetFileRequest(

    /**
     *  File identifier to get info about
     */
    val fileId: String
)


/**
 *  Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels, the
 *  user will not be able to return to the chat on their own using invite links, etc., unless unbanned first. The bot
 *  must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns
 *  True on success.
 */
data class BanChatMemberRequest(

    /**
     *  Unique identifier for the target group or username of the target supergroup or channel (in the format
     *  @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int,

    /**
     *  Date when the user will be unbanned, unix time. If user is banned for more than 366 days or less than 30
     *  seconds from the current time they are considered to be banned forever. Applied for supergroups and channels
     *  only.
     */
    val untilDate: Int? = null,

    /**
     *  Pass True to delete all messages from the chat for the user that is being removed. If False, the user will be
     *  able to see messages in the group that were sent before the user was removed. Always True for supergroups and
     *  channels.
     */
    val revokeMessages: Boolean? = null
)


/**
 *  Use this method to unban a previously banned user in a supergroup or channel. The user will not return to the group
 *  or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this to
 *  work. By default, this method guarantees that after the call the user is not a member of the chat, but will be able
 *  to join it. So if the user is a member of the chat they will also be removed from the chat. If you don't want this,
 *  use the parameter only_if_banned. Returns True on success.
 */
data class UnbanChatMemberRequest(

    /**
     *  Unique identifier for the target group or username of the target supergroup or channel (in the format
     *  @username)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int,

    /**
     *  Do nothing if the user is not banned
     */
    val onlyIfBanned: Boolean? = null
)


/**
 *  Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to
 *  work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a
 *  user. Returns True on success.
 */
data class RestrictChatMemberRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int,

    /**
     *  A JSON-serialized object for new user permissions
     */
    val permissions: ChatPermissions,

    /**
     *  Date when restrictions will be lifted for the user, unix time. If user is restricted for more than 366 days or
     *  less than 30 seconds from the current time, they are considered to be restricted forever
     */
    val untilDate: Int? = null
)


/**
 *  Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the
 *  chat for this to work and must have the appropriate administrator rights. Pass False for all boolean parameters to
 *  demote a user. Returns True on success.
 */
data class PromoteChatMemberRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int,

    /**
     *  Pass True, if the administrator's presence in the chat is hidden
     */
    val isAnonymous: Boolean? = null,

    /**
     *  Pass True, if the administrator can access the chat event log, chat statistics, message statistics in channels,
     *  see channel members, see anonymous administrators in supergroups and ignore slow mode. Implied by any other
     *  administrator privilege
     */
    val canManageChat: Boolean? = null,

    /**
     *  Pass True, if the administrator can create channel posts, channels only
     */
    val canPostMessages: Boolean? = null,

    /**
     *  Pass True, if the administrator can edit messages of other users and can pin messages, channels only
     */
    val canEditMessages: Boolean? = null,

    /**
     *  Pass True, if the administrator can delete messages of other users
     */
    val canDeleteMessages: Boolean? = null,

    /**
     *  Pass True, if the administrator can manage voice chats
     */
    val canManageVoiceChats: Boolean? = null,

    /**
     *  Pass True, if the administrator can restrict, ban or unban chat members
     */
    val canRestrictMembers: Boolean? = null,

    /**
     *  Pass True, if the administrator can add new administrators with a subset of their own privileges or demote
     *  administrators that he has promoted, directly or indirectly (promoted by administrators that were appointed by
     *  him)
     */
    val canPromoteMembers: Boolean? = null,

    /**
     *  Pass True, if the administrator can change chat title, photo and other settings
     */
    val canChangeInfo: Boolean? = null,

    /**
     *  Pass True, if the administrator can invite new users to the chat
     */
    val canInviteUsers: Boolean? = null,

    /**
     *  Pass True, if the administrator can pin messages, supergroups only
     */
    val canPinMessages: Boolean? = null
)


/**
 *  Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns True on
 *  success.
 */
data class SetChatAdministratorCustomTitleRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int,

    /**
     *  New custom title for the administrator; 0-16 characters, emoji are not allowed
     */
    val customTitle: String
)


/**
 *  Use this method to ban a channel chat in a supergroup or a channel. Until the chat is unbanned, the owner of the
 *  banned chat won't be able to send messages on behalf of any of their channels. The bot must be an administrator in
 *  the supergroup or channel for this to work and must have the appropriate administrator rights. Returns True on
 *  success.
 */
data class BanChatSenderChatRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target sender chat
     */
    val senderChatId: Long
)


/**
 *  Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an
 *  administrator for this to work and must have the appropriate administrator rights. Returns True on success.
 */
data class UnbanChatSenderChatRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target sender chat
     */
    val senderChatId: Long
)


/**
 *  Use this method to set default chat permissions for all members. The bot must be an administrator in the group or a
 *  supergroup for this to work and must have the can_restrict_members administrator rights. Returns True on success.
 */
data class SetChatPermissionsRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    val chatId: Long,

    /**
     *  A JSON-serialized object for new default chat permissions
     */
    val permissions: ChatPermissions
)


/**
 *  Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked.
 *  The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
 *  Returns the new invite link as String on success.
 */
data class ExportChatInviteLinkRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long
)


/**
 *  Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for
 *  this to work and must have the appropriate administrator rights. The link can be revoked using the method
 *  revokeChatInviteLink. Returns the new invite link as ChatInviteLink object.
 */
data class CreateChatInviteLinkRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Invite link name; 0-32 characters
     */
    val name: String? = null,

    /**
     *  Point in time (Unix timestamp) when the link will expire
     */
    val expireDate: Int? = null,

    /**
     *  Maximum number of users that can be members of the chat simultaneously after joining the chat via this invite
     *  link; 1-99999
     */
    val memberLimit: Int? = null,

    /**
     *  True, if users joining the chat via the link need to be approved by chat administrators. If True, member_limit
     *  can't be specified
     */
    val createsJoinRequest: Boolean? = null
)


/**
 *  Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the chat
 *  for this to work and must have the appropriate administrator rights. Returns the edited invite link as a
 *  ChatInviteLink object.
 */
data class EditChatInviteLinkRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  The invite link to edit
     */
    val inviteLink: String,

    /**
     *  Invite link name; 0-32 characters
     */
    val name: String? = null,

    /**
     *  Point in time (Unix timestamp) when the link will expire
     */
    val expireDate: Int? = null,

    /**
     *  Maximum number of users that can be members of the chat simultaneously after joining the chat via this invite
     *  link; 1-99999
     */
    val memberLimit: Int? = null,

    /**
     *  True, if users joining the chat via the link need to be approved by chat administrators. If True, member_limit
     *  can't be specified
     */
    val createsJoinRequest: Boolean? = null
)


/**
 *  Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is
 *  automatically generated. The bot must be an administrator in the chat for this to work and must have the
 *  appropriate administrator rights. Returns the revoked invite link as ChatInviteLink object.
 */
data class RevokeChatInviteLinkRequest(

    /**
     *  Unique identifier of the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  The invite link to revoke
     */
    val inviteLink: String
)


/**
 *  Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and
 *  must have the can_invite_users administrator right. Returns True on success.
 */
data class ApproveChatJoinRequestRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int
)


/**
 *  Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work and
 *  must have the can_invite_users administrator right. Returns True on success.
 */
data class DeclineChatJoinRequestRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int
)


/**
 *  Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be
 *  an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on
 *  success.
 */
data class SetChatPhotoRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  New chat photo, uploaded using multipart/form-data
     */
    val photo: InputFile
)


/**
 *  Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator
 *  in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 */
data class DeleteChatPhotoRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long
)


/**
 *  Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an
 *  administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on
 *  success.
 */
data class SetChatTitleRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  New chat title, 1-255 characters
     */
    val title: String
)


/**
 *  Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator
 *  in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 */
data class SetChatDescriptionRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  New chat description, 0-255 characters
     */
    val description: String? = null
)


/**
 *  Use this method to add a message to the list of pinned messages in a chat. If the chat is not a private chat, the
 *  bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right
 *  in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 */
data class PinChatMessageRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Identifier of a message to pin
     */
    val messageId: Int,

    /**
     *  Pass True, if it is not necessary to send a notification to all chat members about the new pinned message.
     *  Notifications are always disabled in channels and private chats.
     */
    val disableNotification: Boolean? = null
)


/**
 *  Use this method to remove a message from the list of pinned messages in a chat. If the chat is not a private chat,
 *  the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator
 *  right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 */
data class UnpinChatMessageRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Identifier of a message to unpin. If not specified, the most recent pinned message (by sending date) will be
     *  unpinned.
     */
    val messageId: Int? = null
)


/**
 *  Use this method to clear the list of pinned messages in a chat. If the chat is not a private chat, the bot must be
 *  an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a
 *  supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 */
data class UnpinAllChatMessagesRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long
)


/**
 *  Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
 */
data class LeaveChatRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup or channel (in the format
     *  @channelusername)
     */
    val chatId: Long
)


/**
 *  Use this method to get up to date information about the chat (current name of the user for one-on-one
 *  conversations, current username of a user, group or channel, etc.). Returns a Chat object on success.
 */
data class GetChatRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup or channel (in the format
     *  @channelusername)
     */
    val chatId: Long
)


/**
 *  Use this method to get a list of administrators in a chat. On success, returns an Array of ChatMember objects that
 *  contains information about all chat administrators except other bots. If the chat is a group or a supergroup and no
 *  administrators were appointed, only the creator will be returned.
 */
data class GetChatAdministratorsRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup or channel (in the format
     *  @channelusername)
     */
    val chatId: Long
)


/**
 *  Use this method to get the number of members in a chat. Returns Int on success.
 */
data class GetChatMemberCountRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup or channel (in the format
     *  @channelusername)
     */
    val chatId: Long
)


/**
 *  Use this method to get information about a member of a chat. Returns a ChatMember object on success.
 */
data class GetChatMemberRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup or channel (in the format
     *  @channelusername)
     */
    val chatId: Long,

    /**
     *  Unique identifier of the target user
     */
    val userId: Int
)


/**
 *  Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for
 *  this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally
 *  returned in getChat requests to check if the bot can use this method. Returns True on success.
 */
data class SetChatStickerSetRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    val chatId: Long,

    /**
     *  Name of the sticker set to be set as the group sticker set
     */
    val stickerSetName: String
)


/**
 *  Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for
 *  this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally
 *  returned in getChat requests to check if the bot can use this method. Returns True on success.
 */
data class DeleteChatStickerSetRequest(

    /**
     *  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    val chatId: Long
)


/**
 *  Use this method to send answers to callback queries sent from inline keyboards. The answer will be displayed to the
 *  user as a notification at the top of the chat screen or as an alert. On success, True is returned.
 */
data class AnswerCallbackQueryRequest(

    /**
     *  Unique identifier for the query to be answered
     */
    val callbackQueryId: String,

    /**
     *  Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
     */
    val text: String? = null,

    /**
     *  If True, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults
     *  to false.
     */
    val showAlert: Boolean? = null,

    /**
     *  URL that will be opened by the user's client. If you have created a Game and accepted the conditions via
     *  @Botfather, specify the URL that opens your game — note that this will only work if the query comes from a
     *  callback_game button.Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a
     *  parameter.
     */
    val url: String? = null,

    /**
     *  The maximum amount of time in seconds that the result of the callback query may be cached client-side. Telegram
     *  apps will support caching starting in version 3.14. Defaults to 0.
     */
    val cacheTime: Int? = null
)


/**
 *  Use this method to change the list of the bot's commands. See https://core.telegram.org/bots#commands for more
 *  details about bot commands. Returns True on success.
 */
data class SetMyCommandsRequest(

    /**
     *  A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be
     *  specified.
     */
    val commands: List<BotCommand>,

    /**
     *  A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to
     *  BotCommandScopeDefault.
     */
    val scope: BotCommandScope? = null,

    /**
     *  A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for
     *  whose language there are no dedicated commands
     */
    val languageCode: String? = null
)


/**
 *  Use this method to delete the list of the bot's commands for the given scope and user language. After deletion,
 *  higher level commands will be shown to affected users. Returns True on success.
 */
data class DeleteMyCommandsRequest(

    /**
     *  A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to
     *  BotCommandScopeDefault.
     */
    val scope: BotCommandScope? = null,

    /**
     *  A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for
     *  whose language there are no dedicated commands
     */
    val languageCode: String? = null
)


/**
 *  Use this method to get the current list of the bot's commands for the given scope and user language. Returns Array
 *  of BotCommand on success. If commands aren't set, an empty list is returned.
 */
data class GetMyCommandsRequest(

    /**
     *  A JSON-serialized object, describing scope of users. Defaults to BotCommandScopeDefault.
     */
    val scope: BotCommandScope? = null,

    /**
     *  A two-letter ISO 639-1 language code or an empty string
     */
    val languageCode: String? = null
)


/**
 *  Use this method to edit text and game messages. On success, if the edited message is not an inline message, the
 *  edited Message is returned, otherwise True is returned.
 */
data class EditMessageTextRequest(

    /**
     *  Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
     *  channel (in the format @channelusername)
     */
    val chatId: Long? = null,

    /**
     *  Required if inline_message_id is not specified. Identifier of the message to edit
     */
    val messageId: Int? = null,

    /**
     *  Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    val inlineMessageId: String? = null,

    /**
     *  New text of the message, 1-4096 characters after entities parsing
     */
    val text: String,

    /**
     *  Mode for parsing entities in the message text. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in message text, which can be specified instead of
     *  parse_mode
     */
    val entities: List<MessageEntity>? = null,

    /**
     *  Disables link previews for links in this message
     */
    val disableWebPagePreview: Boolean? = null,

    /**
     *  A JSON-serialized object for an inline keyboard.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  Use this method to edit captions of messages. On success, if the edited message is not an inline message, the
 *  edited Message is returned, otherwise True is returned.
 */
data class EditMessageCaptionRequest(

    /**
     *  Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
     *  channel (in the format @channelusername)
     */
    val chatId: Long? = null,

    /**
     *  Required if inline_message_id is not specified. Identifier of the message to edit
     */
    val messageId: Int? = null,

    /**
     *  Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    val inlineMessageId: String? = null,

    /**
     *  New caption of the message, 0-1024 characters after entities parsing
     */
    val caption: String? = null,

    /**
     *  Mode for parsing entities in the message caption. See formatting options for more details.
     */
    val parseMode: ParseMode? = null,

    /**
     *  A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
     *  parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

    /**
     *  A JSON-serialized object for an inline keyboard.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  Use this method to edit animation, audio, document, photo, or video messages. If a message is part of a message
 *  album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a
 *  photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously
 *  uploaded file via its file_id or specify a URL. On success, if the edited message is not an inline message, the
 *  edited Message is returned, otherwise True is returned.
 */
data class EditMessageMediaRequest(

    /**
     *  Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
     *  channel (in the format @channelusername)
     */
    val chatId: Long? = null,

    /**
     *  Required if inline_message_id is not specified. Identifier of the message to edit
     */
    val messageId: Int? = null,

    /**
     *  Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    val inlineMessageId: String? = null,

    /**
     *  A JSON-serialized object for a new media content of the message
     */
    val media: InputMedia,

    /**
     *  A JSON-serialized object for a new inline keyboard.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  Use this method to edit only the reply markup of messages. On success, if the edited message is not an inline
 *  message, the edited Message is returned, otherwise True is returned.
 */
data class EditMessageReplyMarkupRequest(

    /**
     *  Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
     *  channel (in the format @channelusername)
     */
    val chatId: Long? = null,

    /**
     *  Required if inline_message_id is not specified. Identifier of the message to edit
     */
    val messageId: Int? = null,

    /**
     *  Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    val inlineMessageId: String? = null,

    /**
     *  A JSON-serialized object for an inline keyboard.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  Use this method to stop a poll which was sent by the bot. On success, the stopped Poll is returned.
 */
data class StopPollRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Identifier of the original message with the poll
     */
    val messageId: Int,

    /**
     *  A JSON-serialized object for a new message inline keyboard.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  Use this method to delete a message, including service messages, with the following limitations:- A message can
 *  only be deleted if it was sent less than 48 hours ago.- A dice message in a private chat can only be deleted if it
 *  was sent more than 24 hours ago.- Bots can delete outgoing messages in private chats, groups, and supergroups.-
 *  Bots can delete incoming messages in private chats.- Bots granted can_post_messages permissions can delete outgoing
 *  messages in channels.- If the bot is an administrator of a group, it can delete any message there.- If the bot has
 *  can_delete_messages permission in a supergroup or a channel, it can delete any message there.Returns True on
 *  success.
 */
data class DeleteMessageRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Identifier of the message to delete
     */
    val messageId: Int
)


/**
 *  This object represents a sticker.
 */
data class Sticker(

    /**
     *  Identifier for this file, which can be used to download or reuse the file
     */
    val fileId: String,

    /**
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
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
     *  File size in bytes
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
     *  Shift by Y-axis measured in heights of the mask scaled to the face size, from top to bottom. For example, 1.0
     *  will place the mask just below the default mask position.
     */
    val yShift: Double,

    /**
     *  Mask scaling coefficient. For example, 2.0 means double size.
     */
    val scale: Double
)


/**
 *  Use this method to send static .WEBP or animated .TGS stickers. On success, the sent Message is returned.
 */
data class SendStickerRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Sticker to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended),
     *  pass an HTTP URL as a String for Telegram to get a .WEBP file from the Internet, or upload a new one using
     *  multipart/form-data. More info on Sending Files »
     */
    val sticker: InputFile,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *  instructions to remove reply keyboard or to force a reply from the user.
     */
    val replyMarkup: Markup? = null
)


/**
 *  Use this method to get a sticker set. On success, a StickerSet object is returned.
 */
data class GetStickerSetRequest(

    /**
     *  Name of the sticker set
     */
    val name: String
)


/**
 *  Use this method to upload a .PNG file with a sticker for later use in createNewStickerSet and addStickerToSet
 *  methods (can be used multiple times). Returns the uploaded File on success.
 */
data class UploadStickerFileRequest(

    /**
     *  User identifier of sticker file owner
     */
    val userId: Int,

    /**
     *  PNG image with the sticker, must be up to 512 kilobytes in size, dimensions must not exceed 512px, and either
     *  width or height must be exactly 512px. More info on Sending Files »
     */
    val pngSticker: InputFile
)


/**
 *  Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus
 *  created. You must use exactly one of the fields png_sticker or tgs_sticker. Returns True on success.
 */
data class CreateNewStickerSetRequest(

    /**
     *  User identifier of created sticker set owner
     */
    val userId: Int,

    /**
     *  Short name of sticker set, to be used in t.me/addstickers/ URLs (e.g., animals). Can contain only english
     *  letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and must end
     *  in “_by_<bot username>”. <bot_username> is case insensitive. 1-64 characters.
     */
    val name: String,

    /**
     *  Sticker set title, 1-64 characters
     */
    val title: String,

    /**
     *  PNG image with the sticker, must be up to 512 kilobytes in size, dimensions must not exceed 512px, and either
     *  width or height must be exactly 512px. Pass a file_id as a String to send a file that already exists on the
     *  Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new
     *  one using multipart/form-data. More info on Sending Files »
     */
    val pngSticker: InputFile? = null,

    /**
     *  TGS animation with the sticker, uploaded using multipart/form-data. See
     *  https://core.telegram.org/animated_stickers#technical-requirements for technical requirements
     */
    val tgsSticker: InputFile? = null,

    /**
     *  One or more emoji corresponding to the sticker
     */
    val emojis: String,

    /**
     *  Pass True, if a set of mask stickers should be created
     */
    val containsMasks: Boolean? = null,

    /**
     *  A JSON-serialized object for position where the mask should be placed on faces
     */
    val maskPosition: MaskPosition? = null
)


/**
 *  Use this method to add a new sticker to a set created by the bot. You must use exactly one of the fields
 *  png_sticker or tgs_sticker. Animated stickers can be added to animated sticker sets and only to them. Animated
 *  sticker sets can have up to 50 stickers. Static sticker sets can have up to 120 stickers. Returns True on success.
 */
data class AddStickerToSetRequest(

    /**
     *  User identifier of sticker set owner
     */
    val userId: Int,

    /**
     *  Sticker set name
     */
    val name: String,

    /**
     *  PNG image with the sticker, must be up to 512 kilobytes in size, dimensions must not exceed 512px, and either
     *  width or height must be exactly 512px. Pass a file_id as a String to send a file that already exists on the
     *  Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new
     *  one using multipart/form-data. More info on Sending Files »
     */
    val pngSticker: InputFile? = null,

    /**
     *  TGS animation with the sticker, uploaded using multipart/form-data. See
     *  https://core.telegram.org/animated_stickers#technical-requirements for technical requirements
     */
    val tgsSticker: InputFile? = null,

    /**
     *  One or more emoji corresponding to the sticker
     */
    val emojis: String,

    /**
     *  A JSON-serialized object for position where the mask should be placed on faces
     */
    val maskPosition: MaskPosition? = null
)


/**
 *  Use this method to move a sticker in a set created by the bot to a specific position. Returns True on success.
 */
data class SetStickerPositionInSetRequest(

    /**
     *  File identifier of the sticker
     */
    val sticker: String,

    /**
     *  New sticker position in the set, zero-based
     */
    val position: Int
)


/**
 *  Use this method to delete a sticker from a set created by the bot. Returns True on success.
 */
data class DeleteStickerFromSetRequest(

    /**
     *  File identifier of the sticker
     */
    val sticker: String
)


/**
 *  Use this method to set the thumbnail of a sticker set. Animated thumbnails can be set for animated sticker sets
 *  only. Returns True on success.
 */
data class SetStickerSetThumbRequest(

    /**
     *  Sticker set name
     */
    val name: String,

    /**
     *  User identifier of the sticker set owner
     */
    val userId: Int,

    /**
     *  A PNG image with the thumbnail, must be up to 128 kilobytes in size and have width and height exactly 100px, or
     *  a TGS animation with the thumbnail up to 32 kilobytes in size; see
     *  https://core.telegram.org/animated_stickers#technical-requirements for animated sticker technical requirements.
     *  Pass a file_id as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a
     *  String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More info
     *  on Sending Files ». Animated sticker set thumbnail can't be uploaded via HTTP URL.
     */
    val thumb: InputFile? = null
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
     *  Text of the query (up to 256 characters)
     */
    val query: String,

    /**
     *  Offset of the results to be returned, can be controlled by the bot
     */
    val offset: String,

    /**
     *  Type of the chat, from which the inline query was sent. Can be either “sender” for a private chat with the
     *  inline query sender, “private”, “group”, “supergroup”, or “channel”. The chat type should be always known for
     *  requests sent from official clients and most third-party clients, unless the request was sent from a secret
     *  chat
     */
    val chatType: String? = null,

    /**
     *  Sender location, only for bots that request user location
     */
    val location: Location? = null
)


/**
 *  Use this method to send answers to an inline query. On success, True is returned.No more than 50 results per query
 *  are allowed.
 */
data class AnswerInlineQueryRequest(

    /**
     *  Unique identifier for the answered query
     */
    val inlineQueryId: String,

    /**
     *  A JSON-serialized array of results for the inline query
     */
    val results: List<InlineQueryResult>,

    /**
     *  The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults
     *  to 300.
     */
    val cacheTime: Int? = null,

    /**
     *  Pass True, if results may be cached on the server side only for the user that sent the query. By default,
     *  results may be returned to any user who sends the same query
     */
    val isPersonal: Boolean? = null,

    /**
     *  Pass the offset that a client should send in the next query with the same text to receive more results. Pass an
     *  empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64
     *  bytes.
     */
    val nextOffset: String? = null,

    /**
     *  If passed, clients will display a button with specified text that switches the user to a private chat with the
     *  bot and sends the bot a start message with the parameter switch_pm_parameter
     */
    val switchPmText: String? = null,

    /**
     *  Deep-linking parameter for the /start message sent to the bot when user presses the switch button. 1-64
     *  characters, only A-Z, a-z, 0-9, _ and - are allowed.Example: An inline bot that sends YouTube videos can ask
     *  the user to connect the bot to their YouTube account to adapt search results accordingly. To do this, it
     *  displays a 'Connect your YouTube account' button above the results, or even before showing any. The user
     *  presses the button, switches to a private chat with the bot and, in doing so, passes a start parameter that
     *  instructs the bot to return an OAuth link. Once done, the bot can offer a switch_inline button so that the user
     *  can easily return to the chat where they wanted to use the bot's inline capabilities.
     */
    val switchPmParameter: String? = null
)


/**
 *  This object represents one result of an inline query. Telegram clients currently support results of the following
 *  20 types:
 *
 *  Note: All URLs passed in inline query results will be available to end users and therefore must be assumed to be
 *  public.
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
     *  A valid URL of the photo. Photo must be in JPEG format. Photo size must not exceed 5MB
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
     *  Duration of the GIF in seconds
     */
    val gifDuration: Int? = null,

    /**
     *  URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
     */
    val thumbUrl: String,

    /**
     *  MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
     *  Video duration in seconds
     */
    val mpeg4Duration: Int? = null,

    /**
     *  URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
     */
    val thumbUrl: String,

    /**
     *  MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
     *  URL of the thumbnail (JPEG only) for the video
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
     *  URL of the thumbnail (JPEG only) for the file
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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
     *  The radius of uncertainty for the location, measured in meters; 0-1500
     */
    val horizontalAccuracy: Double? = null,

    /**
     *  Period in seconds for which the location can be updated, should be between 60 and 86400.
     */
    val livePeriod: Int? = null,

    /**
     *  For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if
     *  specified.
     */
    val heading: Int? = null,

    /**
     *  For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters.
     *  Must be between 1 and 100000 if specified.
     */
    val proximityAlertRadius: Int? = null,

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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
     *  Google Places identifier of the venue
     */
    val googlePlaceId: String? = null,

    /**
     *  Google Places type of the venue. (See supported types.)
     */
    val googlePlaceType: String? = null,

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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
 *
 *  Note: This will only work in Telegram versions released after October 1, 2016. Older clients will not display any
 *  inline results if a game result is among them.
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016 for static stickers and after 06 July,
 *  2019 for animated stickers. Older clients will ignore them.
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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
 *
 *  Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
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
     *  List of special entities that appear in the caption, which can be specified instead of parse_mode
     */
    val captionEntities: List<MessageEntity>? = null,

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
 *  currently support the following 5 types:
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
     *  List of special entities that appear in message text, which can be specified instead of parse_mode
     */
    val entities: List<MessageEntity>? = null,

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
     *  The radius of uncertainty for the location, measured in meters; 0-1500
     */
    val horizontalAccuracy: Double? = null,

    /**
     *  Period in seconds for which the location can be updated, should be between 60 and 86400.
     */
    val livePeriod: Int? = null,

    /**
     *  For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if
     *  specified.
     */
    val heading: Int? = null,

    /**
     *  For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters.
     *  Must be between 1 and 100000 if specified.
     */
    val proximityAlertRadius: Int? = null
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
    val foursquareType: String? = null,

    /**
     *  Google Places identifier of the venue
     */
    val googlePlaceId: String? = null,

    /**
     *  Google Places type of the venue. (See supported types.)
     */
    val googlePlaceType: String? = null
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
 *  Represents the content of an invoice message to be sent as the result of an inline query.
 */
data class InputInvoiceMessageContent(

    /**
     *  Product name, 1-32 characters
     */
    val title: String,

    /**
     *  Product description, 1-255 characters
     */
    val description: String,

    /**
     *  Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal
     *  processes.
     */
    val payload: String,

    /**
     *  Payment provider token, obtained via Botfather
     */
    val providerToken: String,

    /**
     *  Three-letter ISO 4217 currency code, see more on currencies
     */
    val currency: String,

    /**
     *  Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost,
     *  delivery tax, bonus, etc.)
     */
    val prices: List<LabeledPrice>,

    /**
     *  The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For
     *  example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it
     *  shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     *  Defaults to 0
     */
    val maxTipAmount: Int? = null,

    /**
     *  A JSON-serialized array of suggested amounts of tip in the smallest units of the currency (integer, not
     *  float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive,
     *  passed in a strictly increased order and must not exceed max_tip_amount.
     */
    val suggestedTipAmounts: List<Int>? = null,

    /**
     *  A JSON-serialized object for data about the invoice, which will be shared with the payment provider. A detailed
     *  description of the required fields should be provided by the payment provider.
     */
    val providerData: String? = null,

    /**
     *  URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
     *  People like it better when they see what they are paying for.
     */
    val photoUrl: String? = null,

    /**
     *  Photo size
     */
    val photoSize: Int? = null,

    /**
     *  Photo width
     */
    val photoWidth: Int? = null,

    /**
     *  Photo height
     */
    val photoHeight: Int? = null,

    /**
     *  Pass True, if you require the user's full name to complete the order
     */
    val needName: Boolean? = null,

    /**
     *  Pass True, if you require the user's phone number to complete the order
     */
    val needPhoneNumber: Boolean? = null,

    /**
     *  Pass True, if you require the user's email address to complete the order
     */
    val needEmail: Boolean? = null,

    /**
     *  Pass True, if you require the user's shipping address to complete the order
     */
    val needShippingAddress: Boolean? = null,

    /**
     *  Pass True, if user's phone number should be sent to provider
     */
    val sendPhoneNumberToProvider: Boolean? = null,

    /**
     *  Pass True, if user's email address should be sent to provider
     */
    val sendEmailToProvider: Boolean? = null,

    /**
     *  Pass True, if the final price depends on the shipping method
     */
    val isFlexible: Boolean? = null
)


/**
 *  Represents a result of an inline query that was chosen by the user and sent to their chat partner.
 *
 *  Note: It is necessary to enable inline feedback via @Botfather in order to receive these objects in updates.
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
     *  Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
     *  Will be also received in callback queries and can be used to edit the message.
     */
    val inlineMessageId: String? = null,

    /**
     *  The query that was used to obtain the result
     */
    val query: String
)


/**
 *  Use this method to send invoices. On success, the sent Message is returned.
 */
data class SendInvoiceRequest(

    /**
     *  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    val chatId: Long,

    /**
     *  Product name, 1-32 characters
     */
    val title: String,

    /**
     *  Product description, 1-255 characters
     */
    val description: String,

    /**
     *  Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal
     *  processes.
     */
    val payload: String,

    /**
     *  Payments provider token, obtained via Botfather
     */
    val providerToken: String,

    /**
     *  Three-letter ISO 4217 currency code, see more on currencies
     */
    val currency: String,

    /**
     *  Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost,
     *  delivery tax, bonus, etc.)
     */
    val prices: List<LabeledPrice>,

    /**
     *  The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For
     *  example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it
     *  shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
     *  Defaults to 0
     */
    val maxTipAmount: Int? = null,

    /**
     *  A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not
     *  float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive,
     *  passed in a strictly increased order and must not exceed max_tip_amount.
     */
    val suggestedTipAmounts: List<Int>? = null,

    /**
     *  Unique deep-linking parameter. If left empty, forwarded copies of the sent message will have a Pay button,
     *  allowing multiple users to pay directly from the forwarded message, using the same invoice. If non-empty,
     *  forwarded copies of the sent message will have a URL button with a deep link to the bot (instead of a Pay
     *  button), with the value used as the start parameter
     */
    val startParameter: String? = null,

    /**
     *  A JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed
     *  description of required fields should be provided by the payment provider.
     */
    val providerData: String? = null,

    /**
     *  URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
     *  People like it better when they see what they are paying for.
     */
    val photoUrl: String? = null,

    /**
     *  Photo size
     */
    val photoSize: Int? = null,

    /**
     *  Photo width
     */
    val photoWidth: Int? = null,

    /**
     *  Photo height
     */
    val photoHeight: Int? = null,

    /**
     *  Pass True, if you require the user's full name to complete the order
     */
    val needName: Boolean? = null,

    /**
     *  Pass True, if you require the user's phone number to complete the order
     */
    val needPhoneNumber: Boolean? = null,

    /**
     *  Pass True, if you require the user's email address to complete the order
     */
    val needEmail: Boolean? = null,

    /**
     *  Pass True, if you require the user's shipping address to complete the order
     */
    val needShippingAddress: Boolean? = null,

    /**
     *  Pass True, if user's phone number should be sent to provider
     */
    val sendPhoneNumberToProvider: Boolean? = null,

    /**
     *  Pass True, if user's email address should be sent to provider
     */
    val sendEmailToProvider: Boolean? = null,

    /**
     *  Pass True, if the final price depends on the shipping method
     */
    val isFlexible: Boolean? = null,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  A JSON-serialized object for an inline keyboard. If empty, one 'Pay total price' button will be shown. If not
     *  empty, the first button must be a Pay button.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
)


/**
 *  If you sent an invoice requesting a shipping address and the parameter is_flexible was specified, the Bot API will
 *  send an Update with a shipping_query field to the bot. Use this method to reply to shipping queries. On success,
 *  True is returned.
 */
data class AnswerShippingQueryRequest(

    /**
     *  Unique identifier for the query to be answered
     */
    val shippingQueryId: String,

    /**
     *  Specify True if delivery to the specified address is possible and False if there are any problems (for example,
     *  if delivery to the specified address is not possible)
     */
    val ok: Boolean,

    /**
     *  Required if ok is True. A JSON-serialized array of available shipping options.
     */
    val shippingOptions: List<ShippingOption>? = null,

    /**
     *  Required if ok is False. Error message in human readable form that explains why it is impossible to complete
     *  the order (e.g. "Sorry, delivery to your desired address is unavailable'). Telegram will display this message
     *  to the user.
     */
    val errorMessage: String? = null
)


/**
 *  Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the
 *  form of an Update with the field pre_checkout_query. Use this method to respond to such pre-checkout queries. On
 *  success, True is returned. Note: The Bot API must receive an answer within 10 seconds after the pre-checkout query
 *  was sent.
 */
data class AnswerPreCheckoutQueryRequest(

    /**
     *  Unique identifier for the query to be answered
     */
    val preCheckoutQueryId: String,

    /**
     *  Specify True if everything is alright (goods are available, etc.) and the bot is ready to proceed with the
     *  order. Use False if there are any problems.
     */
    val ok: Boolean,

    /**
     *  Required if ok is False. Error message in human readable form that explains the reason for failure to proceed
     *  with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy
     *  filling out your payment details. Please choose a different color or garment!"). Telegram will display this
     *  message to the user.
     */
    val errorMessage: String? = null
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
     *  price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits
     *  past the decimal point for each currency (2 for the majority of currencies).
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
     *  Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$
     *  1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the
     *  decimal point for each currency (2 for the majority of currencies).
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
     *  Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$
     *  1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the
     *  decimal point for each currency (2 for the majority of currencies).
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
     *  Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$
     *  1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the
     *  decimal point for each currency (2 for the majority of currencies).
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
     *  Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     *  used to download or reuse the file.
     */
    val fileUniqueId: String,

    /**
     *  File size in bytes
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
     *  Element type. One of “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport”,
     *  “address”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”,
     *  “temporary_registration”, “phone_number”, “email”.
     */
    val type: String,

    /**
     *  Base64-encoded encrypted Telegram Passport element data provided by the user, available for “personal_details”,
     *  “passport”, “driver_license”, “identity_card”, “internal_passport” and “address” types. Can be decrypted and
     *  verified using the accompanying EncryptedCredentials.
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
     *  Array of encrypted files with documents provided by the user, available for “utility_bill”, “bank_statement”,
     *  “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can be decrypted and
     *  verified using the accompanying EncryptedCredentials.
     */
    val files: List<PassportFile>? = null,

    /**
     *  Encrypted file with the front side of the document, provided by the user. Available for “passport”,
     *  “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the
     *  accompanying EncryptedCredentials.
     */
    val frontSide: PassportFile? = null,

    /**
     *  Encrypted file with the reverse side of the document, provided by the user. Available for “driver_license” and
     *  “identity_card”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
     */
    val reverseSide: PassportFile? = null,

    /**
     *  Encrypted file with the selfie of the user holding a document, provided by the user; available for “passport”,
     *  “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the
     *  accompanying EncryptedCredentials.
     */
    val selfie: PassportFile? = null,

    /**
     *  Array of encrypted files with translated versions of documents provided by the user. Available if requested for
     *  “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”,
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
     *  Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets required for
     *  EncryptedPassportElement decryption and authentication
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
 *  Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able
 *  to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the
 *  error must change). Returns True on success.
 *
 *  Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For
 *  example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc.
 *  Supply some details in the error message to make sure the user knows how to correct the issues.
 */
data class SetPassportDataErrorsRequest(

    /**
     *  User identifier
     */
    val userId: Int,

    /**
     *  A JSON-serialized array describing the errors
     */
    val errors: List<PassportElementError>
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
 *  Use this method to send a game. On success, the sent Message is returned.
 */
data class SendGameRequest(

    /**
     *  Unique identifier for the target chat
     */
    val chatId: Long,

    /**
     *  Short name of the game, serves as the unique identifier for the game. Set up your games via Botfather.
     */
    val gameShortName: String,

    /**
     *  Sends the message silently. Users will receive a notification with no sound.
     */
    val disableNotification: Boolean? = null,

    /**
     *  If the message is a reply, ID of the original message
     */
    val replyToMessageId: Int? = null,

    /**
     *  Pass True, if the message should be sent even if the specified replied-to message is not found
     */
    val allowSendingWithoutReply: Boolean? = null,

    /**
     *  A JSON-serialized object for an inline keyboard. If empty, one 'Play game_title' button will be shown. If not
     *  empty, the first button must launch the game.
     */
    val replyMarkup: InlineKeyboardMarkup? = null
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
 *  Use this method to set the score of the specified user in a game message. On success, if the message is not an
 *  inline message, the Message is returned, otherwise True is returned. Returns an error, if the new score is not
 *  greater than the user's current score in the chat and force is False.
 */
data class SetGameScoreRequest(

    /**
     *  User identifier
     */
    val userId: Int,

    /**
     *  New score, must be non-negative
     */
    val score: Int,

    /**
     *  Pass True, if the high score is allowed to decrease. This can be useful when fixing mistakes or banning
     *  cheaters
     */
    val force: Boolean? = null,

    /**
     *  Pass True, if the game message should not be automatically edited to include the current scoreboard
     */
    val disableEditMessage: Boolean? = null,

    /**
     *  Required if inline_message_id is not specified. Unique identifier for the target chat
     */
    val chatId: Long? = null,

    /**
     *  Required if inline_message_id is not specified. Identifier of the sent message
     */
    val messageId: Int? = null,

    /**
     *  Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    val inlineMessageId: String? = null
)


/**
 *  Use this method to get data for high score tables. Will return the score of the specified user and several of their
 *  neighbors in a game. On success, returns an Array of GameHighScore objects.
 */
data class GetGameHighScoresRequest(

    /**
     *  Target user id
     */
    val userId: Int,

    /**
     *  Required if inline_message_id is not specified. Unique identifier for the target chat
     */
    val chatId: Long? = null,

    /**
     *  Required if inline_message_id is not specified. Identifier of the sent message
     */
    val messageId: Int? = null,

    /**
     *  Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    val inlineMessageId: String? = null
)


/**
 *  This object represents one row of the high scores table for a game.
 *
 *  And that's about all we've got for now.If you've got any questions, please check out our Bot FAQ »
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
