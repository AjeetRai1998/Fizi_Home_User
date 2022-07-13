package digi.coders.thecapsico.helper;
import android.os.Bundle;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import digi.coders.thecapsico.helper.models.ResponseGetMerchantCategory;
import digi.coders.thecapsico.model.AddressResponse.ResponsePlace;
import digi.coders.thecapsico.model.LatLongRespons.ResponseLatLong;
import digi.coders.thecapsico.model.MenuModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyApi {

    @FormUrlEncoded
    @POST("login")
    Call<JsonArray> userLogin(
            @Field("mobile") String mobile,
            @Field("apptoken") String apptoken
    );

    @FormUrlEncoded
    @POST("otpVerification")
    Call<JsonArray> verifyOtp(@Field("mobile") String mobile,
                              @Field("otp") String otp);

    @FormUrlEncoded
    @POST("resendOtp")
    Call<JsonArray> resendOtp(@Field("mobile") String mobile,
                              @Field("apptoken") String apptoken);

    @FormUrlEncoded
    @POST("profile")
    Call<JsonArray> getUserProfile(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("logout")
    Call<JsonArray> logout(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("UpdateProfile")
    Call<JsonArray> updateProfile(@Field("name") String name,
                                  @Field("user_id") String userId,
                                  @Field("email") String email,
                                  @Field("status") String login,
                                  @Field("dob") String dob,
                                  @Field("apptoken") String apptoken);



    @FormUrlEncoded
    @POST("photoUpdate")
    Call<JsonArray> updatePhoto(@Field("icon") String icon,
                                @Field("name") String name,
                                @Field("user_id") String userId);



    @FormUrlEncoded
    @POST("merchant")
    Call<JsonArray> getAllMerchant(@Field("user_id") String userId,
                                   @Field("merchant_category_id") String merchantCategoryid,
                                   @Field("category_id") String categoryId,
                                   @Field("merchant_id") String merchantId,
                                    @Field("latitude") String latitude,
                                   @Field("longitude") String longitude
                                    );

    @FormUrlEncoded
    @POST("highlighted_merchant")
    Call<JsonArray> getHighlightetMerchant(@Field("user_id") String userId,
                                   @Field("merchant_category_id") String merchantCategoryid,
                                           @Field("position") String position

    );

    @FormUrlEncoded
    @POST("GetMerchants")
    Call<JsonArray> getOfferMerchant(@Field("user_id") String userId,
                                   @Field("merchant_category_id") String merchantCategoryid,
                                   @Field("category_id") String categoryId,
                                   @Field("banner_id") String banner_id,
                                   @Field("merchant_id") String merchantId,
                                   @Field("latitude") String latitude,
                                   @Field("longitude") String longitude
    );



    @FormUrlEncoded
    @POST("category")
    Call<JsonArray> getAllCategories(@Field("user_id") String userId,
                                     @Field("merchant_id") String merchantId,
                                     @Field("merchant_category_id") String merchantCategoryId);





    @POST("merchantCategory")
    Call<JsonArray> getMerchantCategory();


    @FormUrlEncoded
    @POST("product")
    Call<JsonArray> getAllProduct(@Field("user_id") String userId,
                                  @Field("category_id") String categoryId);

    @FormUrlEncoded
    @POST("merchantCategory")
    Call<JsonArray> getMerchantsCategory(@Field("user_id") String userId);
    //   @Field("merchant_category_id") String categoryId)




    @FormUrlEncoded
    @POST("addToCart")
    Call<JsonArray> addToCart(@Field("user_id") String userId,
                              @Field("merchant_id") String merchantId,
                              @Field("product_id") String productId,
                              @Field("qty") String quantity,
                              @Field("clear") String d,
                              @Field("addonproduct_id") String addId,

                              @Field("addonproduct_prize") String price,
                              @Field("special_intersections") String specialInstruction
                              );

    @FormUrlEncoded
    @POST("api.php")
    Call<JsonObject> addCart(@Field("user_id") String userId,
                              @Field("flag") String Add,
                             @Field("cust") String cust,
                              @Field("merchant_id") String merchantId,
                              @Field("product_id") String productId,
                              @Field("qty") String quantity,
                              @Field("mrp") String mrp,
                              @Field("price") String price,
                              @Field("sell_price") String sell_price,
                              @Field("discount") String discount,
                              @Field("clear") String d,
                              @Field("status") String status,
                              @Field("addonproduct_id") String addId,
                              @Field("addonproduct_prize") String price1,
                              @Field("scart_id") String scart_id
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<JsonObject> clearCart(
            @Field("flag") String Add,
            @Field("user_id") String userId);



    @FormUrlEncoded
    @POST("cartcount")
    Call<JsonArray> getCartCount(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("cart")
    Call<JsonArray> getCartItem(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("updatequantity")
    Call<JsonArray> updateQuantity(@Field("user_id") String userId,
                                    @Field("merchant_id") String merchantId,
                                   @Field("product_id") String productId,
                                   @Field("qty") String quantity,
                                   @Field("addonproduct_id") String addId,
                                   @Field("addonproduct_prize") String price,
                                   @Field("special_intersections") String specialInstruction);
    @FormUrlEncoded
    @POST("clear")
    Call<JsonArray> clearCart();


    @FormUrlEncoded
    @POST("slider")
    Call<JsonArray> getSlider(@Field("user_id") String userId,
                              @Field("city_id") String city,
                              @Field("latitude") String latitude,
                              @Field("merchant_category_id") String merchant_category_id,
                              @Field("longitude") String longitude
    );


    @FormUrlEncoded
    @POST("ReturnOrder")
    Call<JsonArray> ReturnOrderApi(@Field("user_id") String userId,
                              @Field("order_id") String order_id,
                              @Field("return_messagge") String return_messagge,
                              @Field("product_id") String product_id
    );

    @FormUrlEncoded
    @POST("addAddress")
    Call<JsonArray> addAddress(@Field("user_id") String userId,
                               @Field("latitude") String latitude,
                               @Field("longitude") String longitude,
                               @Field("type") String type,
                               @Field("address") String address,
                               @Field("apartment_no") String apartmentNo,
                               @Field("landmark") String landmark,
                               @Field("pincode") String pincode
                               );







    @FormUrlEncoded
    @POST("updateAddress")
    Call<JsonArray> updateAddress(@Field("address_id") String addressId,
                                         @Field("user_id") String userId,
                                         @Field("latitude") String latitude,
                                         @Field("longitude") String longitude,
                                         @Field("type") String type,
                                         @Field("address") String address,
                                         @Field("apartment_no") String apartmentNo,
                                         @Field("landmark") String landmark,
                                         @Field("pincode") String pincode

                                  );


    @FormUrlEncoded
    @POST("deleteAddress")
    Call<JsonArray> deleteAddress(@Field("address_id") String addresId,@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("address")
    Call<JsonArray> getAllAddress(@Field("user_id") String userId,
                                  @Field("lastaddress") String lastAddress);


    @POST("autocomplete/json?key=AIzaSyCxVS4fkqxueNmz4VyDXqeReIeWD6dK4bo")
    Call<ResponsePlace> getLocations(
            @Query("input") String input
    );

    @POST("details/json?key=AIzaSyCxVS4fkqxueNmz4VyDXqeReIeWD6dK4bo")
    Call<ResponseLatLong> getLatLong(
            @Query("placeid") String input
    );


    @FormUrlEncoded
    @POST("orders")
    Call<JsonArray> order(@Field("merchant_id") String merchantId,
                          @Field("user_id") String userId,
                          @Field("subtotal") String subTotal,
                          @Field("amount") String amount,
                          @Field("method") String method,
                          @Field("address_id") String addressId,
                          @Field("coupon") String coupon,
                          @Field("coupon_discount") String couponDiscount,
                          @Field("shipping_charge") String shippingCharge,
                          @Field("message") String message,
                          @Field("delivery_tip") String tip,
                          @Field("lat") String lat,
                          @Field("lon") String lon,
                          @Field("other_charge") String otherCharge);



    @FormUrlEncoded
    @POST("shippingcharges")
    Call<JsonArray> calculateShippingCharges(@Field("user_id") String userId,
                                            @Field("address_id") String addressId,
                                             @Field("merchant_id") String merchantId);



    @FormUrlEncoded
    @POST("PaymentKey")
    Call<JsonArray> getRazorPayKey(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("onlineorderconfiramtion")
    Call<JsonArray> updateStatus(@Field("order_id") String orerId,
                                 @Field("user_id") String userId,
                                 @Field("payment_status") String paymentStatus,
                                 @Field("txn") String txn,
                                 @Field("payment_response") String response
                                 );





    @FormUrlEncoded
    @POST("myOrders")
    Call<JsonArray> myOrder(@Field("user_id") String userId,
                            @Field("order_id") String orderId);
    @FormUrlEncoded
    @POST("myReturnOrders")
    Call<JsonArray> myReturnOrder(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("myReturnOrders")
    Call<JsonArray> myReturnOrderDetails(
            @Field("user_id") String userId,
            @Field("return_order_id") String return_order_id
    );


    @FormUrlEncoded
    @POST("lastOrder")
    Call<JsonObject> lastOrder(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("LastDeliveredOrder")
    Call<JsonObject> lastDeliveredOrder(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("Coupon")
    Call<JsonArray> getAllCoupons(@Field("user_id") String userId,
                                  @Field("merchant_id") String merchantId,
                                  @Field("latitude") String latitude,
                                  @Field("longitude") String longitude,
                                  @Field("coupon_code") String couponCode);




    @FormUrlEncoded
    @POST("addToWishlist")
    Call<JsonArray> addToWishList(@Field("user_id") String userId,
                                  @Field("merchant_id") String merchantId,
                                  @Field("clear") String clear);

    @FormUrlEncoded
    @POST("wishlist")
    Call<JsonArray> getWishlist(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("rating")
    Call<JsonArray> giveRating(@Field("user_id") String userId,
                               @Field("order_id") String orderId,
                               @Field("remark") String remark,
                               @Field("item_rating") String item_rating,
                               @Field("taste") String taste,
                               @Field("packing") String packing,
                               @Field("quantity") String quantity,
                               @Field("hygine") String hygine,
                               @Field("type") String type,
                               @Field("image") String image,
                               @Field("rated_id") String ratedId,
                               @Field("rating") float rating);

    @FormUrlEncoded
    @POST("MerchantRating")
    Call<JsonArray> merchantRating(@Field("user_id") String userId,
                                   @Field("merchant_id") String merchantId);


    @FormUrlEncoded
    @POST("search")
    Call<JsonArray> search(@Field("user_id") String userId,
                           @Field("latitude") String latitude,
                           @Field("longitude") String longitude,
                           @Field("search_key") String searchKey);

    @FormUrlEncoded
    @POST("searchFood")
    Call<JsonArray> searchFood(@Field("user_id") String userId,
                           @Field("search_key") String searchKey,
                           @Field("rest_id") String rest_id
    );



    @FormUrlEncoded
    @POST("AddonproductList")
    Call<JsonArray> getAddOnProductList(@Field("user_id") String userId,
                                        @Field("merchant_id") String merchantId,
                                        @Field("product_id") String productId);

    @FormUrlEncoded
    @POST("apptokans")
    Call<JsonArray> sentToken(@Field("user_id") String userId,
                              @Field("token") String token);


    @FormUrlEncoded
    @POST("addtowallet")
    Call<JsonArray> addToWallet(@Field("user_id") String userId,
                                @Field("amount") String amount
                                );

    @FormUrlEncoded
    @POST("paymentconfirmation")
    Call<JsonArray> paymentConfirmation(@Field("user_id") String userId,
                                        @Field("txt_id") String transactionId,
                                        @Field("order_id") String orderId,
                                        @Field("payment_response") String paymentReponse,
                                        @Field("payment_status") String paymentStatus);

    @FormUrlEncoded
    @POST("transactionhistory")
    Call<JsonArray> getTransactionHistory(@Field("user_id") String userId);



    @FormUrlEncoded
    @POST("categoriesWiseproduct")
    Call<JsonArray> getCategoryWiseProduct(@Field("user_id") String userId,
                                        @Field("merchant_id") String merchantId);




    @FormUrlEncoded
    @POST("Reorder")
    Call<JsonArray> reorderFood(@Field("user_id") String userId,
                                @Field("order_id") String orderId);


    @FormUrlEncoded
    @POST("userApi.php")
    Call<JsonObject> addNumber(@Field("number") String number,
                                @Field("order_id") String orderId,
                                @Field("flag") String flag
    );



    @FormUrlEncoded
    @POST("CancelOrder")
    Call<JsonArray> cancelOrder(@Field("user_id") String userId,
                                @Field("order_id") String orderId);


    @FormUrlEncoded
    @POST("MissedOrder")
    Call<JsonArray> missedOrder(@Field("user_id") String userId,
                                @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("checkcity")
    Call<JsonArray> checkCity(@Field("user_id") String userId,
                              @Field("latitude") String latitude,
                              @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("checkcityoncart")
    Call<JsonArray> checkCityOnCart(@Field("user_id") String userId,
                              @Field("merchant_id") String merchant_id,
                              @Field("latitude") String latitude,
                              @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("AssignOrderMerchant")
    Call<JsonArray> assignOrder(@Field("user_id") String userId,
                                @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("generatetoken")
    Call<JsonArray> generateToken(@Field("subject") String subject,
                                  @Field("message") String message,
                                  @Field("order_id") String order,
                                  @Field("user_id") String userId);


    @FormUrlEncoded
    @POST("mytoken")
    Call<JsonArray> getMyToken(@Field("user_id") String userId);



    @FormUrlEncoded
    @POST("product")
    Call<JsonArray> getProductDetails(@Field("user_id") String userId,
                                      @Field("product_id") String productId);




    @FormUrlEncoded
    @POST("OrderStatus")
    Call<JsonArray> getOrderStatus(@Field("order_id") String orderId,
                                   @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("app_update.php")
    Call<JsonObject> getUpdate(@Field("app_name") String orderId);

    @FormUrlEncoded
    @POST("getAbout.php")
    Call<JsonObject> getAbout(@Field("name") String orderId);

    @FormUrlEncoded
    @POST("submitFeedback.php")
    Call<JsonObject> submitFeedback(
            @Field("user_id") String user_id,
            @Field("name") String orderId
            );

    @FormUrlEncoded
    @POST("getCity.php")
    Call<JsonObject> getCity(@Field("city") String orderId);

    @FormUrlEncoded
    @POST("RestaurantMenu")
    Call<JsonArray> getMenus(
            @Field("user_id") String user_id,
            @Field("restaurant_id") String restaurant_id,
            @Field("offset") String offset,
            @Field("limit") String limit
    );

    @FormUrlEncoded
    @POST("banner")
    Call<JsonArray> getOfferBanner1(
            @Field("user_id") String user_id,
            @Field("city_id") String city_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @FormUrlEncoded
    @POST("banner2")
    Call<JsonArray> getOfferBanner2(
            @Field("user_id") String user_id,
            @Field("city_id") String city_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @FormUrlEncoded
    @POST("Reorder")
    Call<JsonArray> reorder(
            @Field("user_id") String user_id,
            @Field("order_id") String order_id
    );

    @FormUrlEncoded
    @POST("missed_orders.php")
    Call<JsonObject> orderStatus(@Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("updatevarsion")
    Call<JsonArray> updateVersionName(
            @Field("user_id") String userId,
            @Field("userid") String userid,
            @Field("version_code") String version_code);

    @FormUrlEncoded
    @POST("get_coupon_count")
    Call<JsonArray> checkCouponValid(
            @Field("user_id") String userId,
            @Field("coupon_id") String coupon_id);
}