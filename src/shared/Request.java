package shared;

import java.io.Serializable;

public class Request implements Serializable {
    private String title;
    private RequestType requestType;

    public Request(String title, RequestType requestType) {
        this.title = title;
        this.requestType = requestType;
    }

    public String getTitle() {
        return title;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
