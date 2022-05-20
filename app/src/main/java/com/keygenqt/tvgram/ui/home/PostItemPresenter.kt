package com.keygenqt.tvgram.ui.home

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import coil.load
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.data.HomeModel
import com.keygenqt.tvgram.extensions.toDate
import org.drinkless.td.libcore.telegram.TdApi


/**
 * Presenter for post items
 */
class PostItemPresenter(val context: Context) : Presenter() {

    companion object {
        const val CARD_WIDTH = 500
        const val CARD_HEIGHT = 350
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val cardView = object : ImageCardView(context) {}
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {

        val model = item as HomeModel
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = when (val content = model.chat.lastMessage?.content) {
            is TdApi.MessageVoiceNote -> content.caption.text.ifBlank { "A voice message" }
            is TdApi.MessageChatDeleteMember -> "Delete user: ${content.userId}"
            is TdApi.MessageText -> content.text.text
            is TdApi.MessagePhoto -> content.caption.text
            is TdApi.MessageVideo -> content.caption.text
            else -> null
        }

        model.chat.lastMessage?.content?.let {
            cardView.contentText = context.getString(
                R.string.home_item_type,
                model.chat.lastMessage?.date?.toDate(),
                it::class.java.simpleName.replace("Message", "")
            )
        }
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)

//        model.fileImage?.local?.path?.let {
//            cardView.mainImageView.load(model.fileImage?.local?.path)
//        } ?: run {
            cardView.mainImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.dr_message_type_text))
//        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
    }

}