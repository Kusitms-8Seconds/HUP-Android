package com.me.hurryuphup.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Constants {

    public static Long userId = null;
    public static String accessToken = null;
    public static String refreshToken = null;
    public static String targetToken = null;
    public static final String imageBaseUrl = "https://hup-bucket.s3.ap-northeast-2.amazonaws.com/";

    @Getter
    @NoArgsConstructor
    public enum ESignUp{
        emailDuplicateMessage("이미 사용하고 있는 이메일입니다."),
        emailNonDuplicateMessage("사용가능한 이메일입니다."),
        emailFormatErrorMessage("이메일을 알맞은 형태로 입력해주세요."),
        emailLetterCountError("이메일을 10~20자로 입력해주세요."),
        emailFormat("\\w+@\\w+\\.\\w+(\\.\\w+)?"),
        pwEnglishNumberFormat("^.*(?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$"),
        idWarningMessage("아이디는 최소 5글자 이상 10글자 이하여야 합니다."),
        idDuplicateMessage("이미 사용하고 있는 아이디입니다."),
        pwInputMessage("비밀번호창에 비밀번호를 입력해주세요."),
        pwWarningMessage("비밀번호는 영어, 숫자, 특수문자를 조합해 8~12자리로 입력해주세요."),
        pwNotMatchMessage("비밀번호가 서로 일치하지 않습니다."),
        nameInputMessage("이름을 입력해주세요."),
        nameInput2Message("이름은 최소 2글자 이상 10글자 이하여야 합니다."),
        phoneNumberInputMessage("휴대폰번호를 입력해주세요,"),
        phoneNumberFormat("^\\d{3}-\\d{3,4}-\\d{4}$"),
        phoneNumberErrorMessage("올바른 휴대폰 번호를 입력해주세요."),
        genderCheckMessage("성별을 체크해주세요."),
        agreeCheckMessage("약관에 동의해주세요."),
        completeSignUpMessage("회원가입을 완료했습니다."),
        dpYear("년"),
        dpMonth("월"),
        dpDay("일"),
        emailAppType("APP"),
        userEmail("userEmail"),
        userPassword("userPassword");

        private String text;
        private ESignUp(String text){
            this.text=text;
        }
        public String getText(){
            return this.text;
        }

    }

    @Getter
    @NoArgsConstructor
    public enum ELoginType{
        eGoogle("구글"),
        eNaver("네이버"),
        eKakao("카카오"),
        eApp("앱");

//        private final String name;

        private String text;
        private ELoginType(String text){
            this.text=text;
        }
        public String getText(){
            return this.text;
        }
    }

    @Getter
    @NoArgsConstructor
    public enum ELoginCallback{
        TAG("LoginCallback:"),
        eGoogleRequestIdToken("221537301769-e1qd8130nulhheiqo68nv8upistikcp4.apps.googleusercontent.com"),

        eSuccessResponse("login retrofit success, idToken: "),
        eFailResponse("onFailResponse"),
        eConnectionFail("연결실패"),

        eKakaoTAG("KAKAO_API"),
        eGoogleTAG("GOOGLE_API"),
        eNaverTAG("NAVER_API"),

        ePersonId("사용자 아이디: "),
        eEmail("email: "),
        eNickname("nickname: "),
        ePersonGivenName("personGivenName: "),
        ePersonFamilynName("personFamilynName: "),
        eProfilImg("profile image: "),
        eThumbnailImg("thumbnail image: "),
        eAccessToken("accessToken: "),
        eRefreshToken("refreshToken : "),
        eExpiresAt("expiresAt: "),
        eTokenType("tokenType : "),

        eKakaoSessionCallback("SessionCallback :: "),
        eSessionOpenFailed("onSessionOpenFailed: "),
        eKakaoSessionError("세션이 닫혀 있음: "),
        eKakaoUserError("사용자 정보 요청 실패: "),
        eGoogleErrorCode("signInResult:failed code=");

        private String text;
        private ELoginCallback(String text){
            this.text=text;
        }
        public String getText(){
            return this.text;
        }
    }

    @Getter
    public enum EUserServiceImpl{
        eAlreadyRegisteredEmailExceptionMessage("이미 가입되어 있는 이메일입니다."),
        eAlreadyRegisteredUserExceptionMessage("이미 가입되어 있는 유저입니다."),
        eAlreadyRegisteredLoginIdExceptionMessage("이미 등록되어 있는 아이디입니다."),
        eSuccessSignUpMessage("회원가입을 완료했습니다."),
        eUsernameNotFoundExceptionMessage(" -> 유저 이름을 데이터베이스에서 찾을 수 없습니다."),
        eUserNotActivatedExceptionMessage(" -> 유저가 활성화되어 있지 않습니다."),
        eNotFoundUserExceptionMessage("해당 유저아이디로 유저를 찾을 수 없습니다."),
        eNotFoundRegisteredUserExceptionMessage("해당 이메일로 회원가입된 유저가 없습니다."),
        eNotActivatedEmailAuthExceptionMessage("이메일 인증이 완료되지 않았습니다."),
        eNotDuplicatedLoginIdMessage("해당 아이디로 회원가입을 할 수 있습니다."),
        eRefreshToken("RT:"),
        eNotValidRefreshTokenExceptionMessage("Refresh Token 정보가 유효하지 않습니다."),
        eNotMatchRefreshTokenExceptionMessage("Refresh Token 정보가 일치하지 않습니다."),
        eWrongRefreshTokenRequestExceptionMessage("잘못된 요청입니다."),
        eLogout("logout"),
        eNotValidAccessTokenExceptionMessage("만료된 토큰입니다."),
        eLogoutMessage("로그아웃 되었습니다."),
        eTrue(true),
        eAuthorityRoleUser("ROLE_USER"),
        eBaseFileURL("http://www.hurryuphup.me/api/v1/files/"),
        eBasePicture("https://firebasestorage.googleapis.com/v0/b/auctionapp-f3805.appspot.com/o/profile.png?alt=media&token=655ed158-b464-4e5e-aa56-df3d7f12bdc8"),
        e403Error("Forbidden");

        private boolean check;
        private String value;

        EUserServiceImpl(boolean check) { this.check = check;}
        EUserServiceImpl(String value) {this.value = value;}

    }
    @Getter
    @AllArgsConstructor
    public enum EOAuth2UserServiceImpl{
        eRoleUser("ROLE_USER"),
        eGoogleIdAttribute("id"),
        eGoogleKeyAttribute("key"),
        eGoogleNameAttribute("name"),
        eGoogleEmailAttribute("email"),
        eGooglePictureAttribute("picture"),
        eGoogleSub("sub"),
        eGoogle("google"),
        eGoogleInvalidIdTokenMessage("ID token이 유효하지 않습니다."),
        eKakaoKeyAttribute("key"),
        eKakaoProfile("profile"),
        eKakaoNickNameAttribute("nickname"),
        eKakaoEmailAttribute("email"),
        eKakaoProfileImageAttribute("profile_image_url"),
        eKakaoGetMethod("GET"),
        eKakaoAuthorization("Authorization"),
        eKakaoBearer("Bearer "),
        eKakaoContentType("Content-Type"),
        eKakaoContentTypeUrlencoded("application/x-www-form-urlencoded"),
        eKakaoResponseCode("responseCode : "),
        eKakaoEmpty(""),
        eKakaoPropertiesAttribute("properties"),
        eKakaoAccountAttribute("kakao_account"),
        eKakao("kakao"),
        eNaverNameAttribute("name"),
        eNaverEmailAttribute("email"),
        eNaverProfileImageAttribute("profile_image"),
        eNaverKeyAttribute("key"),
        eNaverGetMethod("GET"),
        eNaverResponse("response"),
        eNaverElement("element"),
        eNaverAuthorization("Authorization"),
        eNaverBearer("Bearer "),
        eNaverApiUrlException("API URL이 잘못되었습니다. : "),
        eNaverConnectionException("연결이 실패했습니다. : "),
        eNaverApiResponseException("API 응답을 읽는데 실패했습니다."),
        eNaverNull(null),
        eNaver("naver");

        private final String value;

        public static EOAuth2UserServiceImpl from(String s) {
            return EOAuth2UserServiceImpl.valueOf(s);
        }
    }
}
