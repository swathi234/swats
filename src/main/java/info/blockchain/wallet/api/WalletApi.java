package info.blockchain.wallet.api;

import info.blockchain.wallet.BlockchainFramework;
import info.blockchain.wallet.api.data.FeesBody;
import info.blockchain.wallet.api.data.FeesListBody;
import info.blockchain.wallet.api.data.MerchantBody;
import info.blockchain.wallet.api.data.SettingsBody;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import retrofit2.Call;

public class WalletApi {

    private static WalletEndpoints walletApi;
    private static WalletEndpoints walletServer;

    private static WalletEndpoints getBaseApiInstance() {
        if (walletApi == null) {
            walletApi = BlockchainFramework.getRetrofitApiInstance().create(WalletEndpoints.class);
        }
        return walletApi;
    }

    private static WalletEndpoints getServerApiInstance() {
        if (walletServer == null) {
            walletServer = BlockchainFramework.getRetrofitServerInstance()
                .create(WalletEndpoints.class);
        }
        return walletServer;
    }

    public static Call<FeesListBody> getDynamicFee() {
        return getBaseApiInstance().getFees();
    }

    public static FeesBody getDefaultFee() throws IOException {
        return FeesBody.fromJson(""
            + "{\n"
            + "     \"fee\": 35000,\n"
            + "     \"surge\": false,\n"
            + "     \"ok\": true\n"
            + "}");
    }

    public static Call<ResponseBody> getRandomBytes() {
        return getBaseApiInstance().getRandomBytes(32, "hex");
    }

    public static Call<ResponseBody> updateFirebaseNotificationToken(String token, String guid,
        String sharedKey)
        throws Exception {

        return getServerApiInstance().postToWallet("update-firebase",
            guid,
            sharedKey,
            token,
            token.length(),
            BlockchainFramework.getApiCode());
    }

    public static Call<ResponseBody> registerMdid(String guid, String sharedKey,
        String signedGuid) {
        return getServerApiInstance().postToWallet("register-mdid",
            guid, sharedKey, signedGuid, signedGuid.length(),
            BlockchainFramework.getApiCode());
    }

    public static Call<ResponseBody> unregisterMdid(String guid, String sharedKey,
        String signedGuid) {
        return getServerApiInstance().postToWallet("unregister-mdid",
            guid, sharedKey, signedGuid, signedGuid.length(),
            BlockchainFramework.getApiCode());
    }

    public static Call<Void> setAccess(String key, String pin) {
        return getBaseApiInstance().pinStore(key, pin, "put", BlockchainFramework.getApiCode());
    }

    public static Call<Void> validateAccess(String key, String pin) {
        return getBaseApiInstance().pinStore(key, pin, "get", BlockchainFramework.getApiCode());
    }

    @Deprecated
    public static Call<Void> saveWallet(boolean isNew, String guid, String sharedKey,
        List<String> activeAddressList, JSONObject encryptedPayload,
        boolean syncPubkeys, String newChecksum, String oldChecksum, String email, String device)
        throws UnsupportedEncodingException {

        String pipedAddresses = null;
        if (syncPubkeys && activeAddressList != null) {
            pipedAddresses = StringUtils.join(activeAddressList, "|");
        }

        String method = isNew ? "insert" : "update";

        return getServerApiInstance().syncWallet(method,
            guid, sharedKey, encryptedPayload.toString(), encryptedPayload.toString().length(),
            URLEncoder.encode(newChecksum, "utf-8"), pipedAddresses, email, device, oldChecksum,
            BlockchainFramework.getApiCode());
    }

    public static Call<Void> insertWallet(String guid, String sharedKey,
        @Nullable List<String> activeAddressList, String encryptedPayload,
        String newChecksum, String email, String device)
        throws UnsupportedEncodingException {

        String pipedAddresses = null;
        if (activeAddressList != null) {
            pipedAddresses = StringUtils.join(activeAddressList, "|");
        }

        return getServerApiInstance().syncWallet("insert",
            guid, sharedKey, encryptedPayload, encryptedPayload.length(),
            URLEncoder.encode(newChecksum, "utf-8"), pipedAddresses, email, device, null,
            BlockchainFramework.getApiCode());
    }

    public static Call<Void> updateWallet(String guid, String sharedKey,
        @Nullable List<String> activeAddressList, String encryptedPayload,
        String newChecksum, String oldChecksum, String device)
        throws UnsupportedEncodingException {

        String pipedAddresses = null;
        if (activeAddressList != null) {
            pipedAddresses = StringUtils.join(activeAddressList, "|");
        }

        return getServerApiInstance().syncWallet("update",
            guid, sharedKey, encryptedPayload, encryptedPayload.length(),
            URLEncoder.encode(newChecksum, "utf-8"), pipedAddresses, null, device, oldChecksum,
            BlockchainFramework.getApiCode());
    }

    public static Call<ResponseBody> fetchWalletData(String guid, String sharedKey) {
        return getServerApiInstance().fetchWalletData("wallet.aes.json",
            guid, sharedKey,
            "json",
            BlockchainFramework.getApiCode());
    }

    public static Call<ResponseBody> fetchEncryptedPayload(String guid) {
        return getServerApiInstance().fetchEncryptedPayload(guid,
            "json",
            false,
            BlockchainFramework.getApiCode());
    }

    public static Call<ResponseBody> fetchPairingEncryptionPassword(final String guid) {
        return getServerApiInstance().fetchPairingEncryptionPassword("pairing-encryption-password",
            guid,
            BlockchainFramework.getApiCode());
    }

    public static Call<ArrayList<MerchantBody>> getAllMerchants() throws Exception {
        return getBaseApiInstance().getAllMerchants();
    }

    public static Call<SettingsBody> fetchSettings(String method, String guid, String sharedKey){
        return getServerApiInstance().fetchSettings(method,
            guid, sharedKey,"plain",
            BlockchainFramework.getApiCode());
    }

    public static Call<ResponseBody> updateSettings(String method, String guid, String sharedKey, String payload){
        return getServerApiInstance().updateSettings(method,
            guid, sharedKey,
            payload,
            payload.length(),
            "plain",
            BlockchainFramework.getApiCode());
    }
}