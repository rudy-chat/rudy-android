package org.rudy.chat.ui.Stories;

import android.text.TextUtils;

import org.rudy.chat.messenger.ChatObject;
import org.rudy.chat.messenger.MessagesController;
import org.rudy.chat.tgnet.TLRPC;

public class ChannelBoostUtilities {
    public static String createLink(int currentAccount, long dialogId) {
        TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-dialogId);
        String username = ChatObject.getPublicUsername(chat);
        if (!TextUtils.isEmpty(username)) {
            return "https://rudy.chat/boost/" + ChatObject.getPublicUsername(chat);
        } else {
            return "https://rudy.chat/boost/?c=" + -dialogId;
        }
    }
}
