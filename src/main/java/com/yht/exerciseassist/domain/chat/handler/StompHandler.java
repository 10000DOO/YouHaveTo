package com.yht.exerciseassist.domain.chat.handler;

import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.ValidationTokenSign;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = "";
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
                token = bearerToken.substring(7);
            }
            if(jwtTokenProvider.validateToken(token) != ValidationTokenSign.VALID)
                throw new AccessDeniedException(ErrorCode.JWT_AUTHENTICATION_FAIL.getMessage());
        }
        return message;
    }
}
