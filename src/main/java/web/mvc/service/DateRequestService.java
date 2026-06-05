package web.mvc.service;

public interface DateRequestService {

    void sendRequest();
    void getReceivedRequestsByBoard();
    void getAllReceivedRequests();
    void getSentRequests();
    void acceptRequest();
    void rejectRequest();
    void cancelRequest();
}
