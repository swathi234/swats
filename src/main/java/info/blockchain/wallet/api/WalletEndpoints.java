package info.blockchain.wallet.api;

import info.blockchain.wallet.api.data.FeesList;
import info.blockchain.wallet.api.data.Merchant;
import info.blockchain.wallet.api.data.Settings;
import info.blockchain.wallet.api.data.Status;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WalletEndpoints {


    @POST("wallet")
    Call<ResponseBody> postToWallet(
        @Query("method") String method,
        @Query("guid") String guid,
        @Query("sharedKey") String sharedKey,
        @Query("payload") String payload,
        @Query("length") int length,
        @Query("api_code") String apiCode);

    @POST("wallet")
    Call<Settings> fetchSettings(
        @Query("method") String method,
        @Query("guid") String guid,
        @Query("sharedKey") String sharedKey,
        @Query("format") String format,
        @Query("api_code") String apiCode);

    @POST("wallet")
    Call<ResponseBody> updateSettings(
        @Query("method") String method,
        @Query("guid") String guid,
        @Query("sharedKey") String sharedKey,
        @Query("payload") String payload,
        @Query("length") int length,
        @Query("format") String format,
        @Query("api_code") String apiCode);

    @POST("wallet")
    Call<ResponseBody> fetchWalletData(
        @Query("method") String method,
        @Query("guid") String guid,
        @Query("sharedKey") String sharedKey,
        @Query("format") String format,
        @Query("api_code") String apiCode);

    @POST("wallet")
    Call<Void> syncWallet(
        @Query("method") String method,
        @Query("guid") String guid,
        @Query("sharedKey") String sharedKey,
        @Query("payload") String payload,
        @Query("length") int length,
        @Query("checksum") String checksum,
        @Query("active") String active,
        @Query("email") String email,
        @Query("device") String device,
        @Query("old_checksum") String old_checksum,
        @Query("api_code") String apiCode);

    @POST("wallet")
    Call<ResponseBody> fetchPairingEncryptionPassword(
        @Query("method") String method,
        @Query("guid") String guid,
        @Query("api_code") String apiCode);

    @GET("wallet/{guid}")
    Call<ResponseBody> fetchEncryptedPayload(
        @Path("guid") String guid,
        @Query("format") String format,
        @Query("resend_code") boolean resendCode,
        @Query("api_code") String apiCode);

    @GET("fees")
    Call<FeesList> getFees();

    @GET("v2/randombytes")
    Call<ResponseBody> getRandomBytes(
        @Query("bytes") int bytes,
        @Query("format") String format);

    @POST("pin-store")
    Call<Status> pinStore(
        @Query("key") String key,
        @Query("pin") String pin,
        @Query("value") String value,
        @Query("method") String method,
        @Query("api_code") String apiCode);

    @GET("merchant")
    Call<ArrayList<Merchant>> getAllMerchants();

    @GET("frombtc")
    Call<ResponseBody> getHistoricPrice(
        @Query("value") long value,
        @Query("currency") String currency,
        @Query("time") long time,
        @Query("api_code") String apiCode);
}