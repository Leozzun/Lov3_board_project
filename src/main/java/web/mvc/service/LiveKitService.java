package web.mvc.service;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LiveKitService {

    @Value("${livekit.api-key}")
    private String apiKey;

    @Value("${livekit.api-secret}")
    private String apiSecret;

    public String generateToken(String roomName, String userName) {
        AccessToken token = new AccessToken(apiKey, apiSecret);
        token.setName(userName);
        token.setIdentity(userName);
        token.addGrants(
            new RoomJoin(true),
            new RoomName(roomName)
        );
        return token.toJwt();
    }
}
