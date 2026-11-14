package org.rudy.chat.messenger.voip;

import static org.rudy.chat.messenger.voip.VoIPService.getStringFromFile;

import android.text.TextUtils;

import org.rudy.chat.messenger.AndroidUtilities;
import org.rudy.chat.messenger.BuildVars;
import org.rudy.chat.messenger.FileLoader;
import org.rudy.chat.messenger.FileLog;
import org.rudy.chat.messenger.Utilities;
import org.rudy.chat.tgnet.ConnectionsManager;
import org.rudy.chat.tgnet.TLRPC;
import org.rudy.chat.tgnet.tl.TL_phone;
import org.rudy.chat.ui.Components.voip.VoIPHelper;

import java.io.File;
import java.util.HashMap;

public class VoIPDebugToSend {

    private final int currentAccount;
    public VoIPDebugToSend(int account) {
        this.currentAccount = account;
    }

    private final class Data {
        long callId;
        long access_hash;
        Instance.FinalState state;
        String logPath;
    }

    private final HashMap<Long, Data> pending = new HashMap<>();

    public void push(long callId, long access_hash, Instance.FinalState state, String logPath) {
        if (TextUtils.isEmpty(state.debugLog)) {
            try {
                state.debugLog = getStringFromFile(VoIPHelper.getLogFilePath("" + callId, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final Data data = new Data();
        data.callId = callId;
        data.access_hash = access_hash;
        data.state = state;
        data.logPath = logPath;
        pending.put(callId, data);
    }

    public void done(long callId, boolean need_debug) {
        final Data data = pending.remove(callId);
        if (data == null || !need_debug) return;

        TL_phone.saveCallDebug req = new TL_phone.saveCallDebug();
        req.debug = new TLRPC.TL_dataJSON();
        req.debug.data = data.state.debugLog;
        req.peer = new TLRPC.TL_inputPhoneCall();
        req.peer.access_hash = data.access_hash;
        req.peer.id = data.callId;
        ConnectionsManager.getInstance(currentAccount).sendRequest(req, (response, error) -> {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Sent debug logs, response = " + response);
            }
            if (response instanceof TLRPC.TL_boolFalse && !TextUtils.isEmpty(data.logPath)) {
                final File gzippedLog = new File(data.logPath + ".gzip");
                Utilities.searchQueue.postRunnable(() -> {
                    if (!AndroidUtilities.gzip(new File(data.logPath), gzippedLog))
                        return;
                    AndroidUtilities.runOnUIThread(() -> {
                        FileLoader.getInstance(currentAccount).uploadFile(gzippedLog.getAbsolutePath(), file -> {
                            if (file == null) return;

                            final TL_phone.saveCallLog req2 = new TL_phone.saveCallLog();
                            req2.peer = req.peer;
                            req2.file = file;
                            ConnectionsManager.getInstance(currentAccount).sendRequest(req2, null);
                        });
                    });
                });
            }
        });
    }

}
