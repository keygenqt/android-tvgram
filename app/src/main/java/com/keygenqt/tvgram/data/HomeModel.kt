package com.keygenqt.tvgram.data

import org.drinkless.td.libcore.telegram.TdApi

//enum class ChatMessageTypes {
//    MessageText,
//    MessageAnimation,
//    MessageAudio,
//    MessageDocument,
//    MessagePhoto,
//    MessageExpiredPhoto,
//    MessageSticker,
//    MessageVideo,
//    MessageExpiredVideo,
//    MessageVideoNote,
//    MessageVoiceNote,
//    MessageLocation,
//    MessageVenue,
//    MessageContact,
//    MessageAnimatedEmoji,
//    MessageDice,
//    MessageGame,
//    MessagePoll,
//    MessageInvoice,
//    MessageCall,
//    MessageVideoChatScheduled,
//    MessageVideoChatStarted,
//    MessageVideoChatEnded,
//    MessageInviteVideoChatParticipants,
//    MessageBasicGroupChatCreate,
//    MessageSupergroupChatCreate,
//    MessageChatChangeTitle,
//    MessageChatChangePhoto,
//    MessageChatDeletePhoto,
//    MessageChatAddMembers,
//    MessageChatJoinByLink,
//    MessageChatJoinByRequest,
//    MessageChatDeleteMember,
//    MessageChatUpgradeTo,
//    MessageChatUpgradeFrom,
//    MessagePinMessage,
//    MessageScreenshotTaken,
//    MessageChatSetTheme,
//    MessageChatSetTtl,
//    MessageCustomServiceAction,
//    MessageGameScore,
//    MessagePaymentSuccessful,
//    MessagePaymentSuccessfulBot,
//    MessageContactRegistered,
//    MessageWebsiteConnected,
//    MessagePassportDataSent,
//    MessagePassportDataReceived,
//    MessageProximityAlertTriggered,
//    MessageUnsupported,
//    MessageNull,
//}

/**
 * Model menu settings
 *
 * @property chat [TdApi.Chat]
 * @property fileImage [TdApi.File]
 */
data class HomeModel(
    val chat: TdApi.Chat,
    val message: TdApi.Message?,
    var fileImage: TdApi.File?
)