package web.mvc.service;

public interface MemberService {

    void signUp();
    void checkDuplicateId();
    void getMyInfo();
    void updateMyInfo();
    void deleteMyAccount();
    void updateProfile();
    void getProfile();
    void getAllMembers();
    void deleteMemberByAdmin();
}
