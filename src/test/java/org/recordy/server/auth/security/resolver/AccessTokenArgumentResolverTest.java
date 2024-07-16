package org.recordy.server.auth.security.resolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AccessTokenArgumentResolverTest {

    private AccessTokenArgumentResolver resolver;

    @Mock
    private ModelAndViewContainer mavContainer;

    @Mock
    private NativeWebRequest webRequest;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        resolver = new AccessTokenArgumentResolver();
    }

    public void testMethod(@UserId Long userId) {
        // test method to create MethodParameter
    }

    public void testMethodWithoutAnnotation(Long userId) {
        // 테스트용 메서드
    }

    public void testMethodWithWrongType(@UserId String userId) {
        // 테스트용 메서드
    }

    @Test
    void supportsParameter를_통해_UserId_타입을_지원할_경우_true를_반환한다() throws Exception {
        // given
        Method method = this.getClass().getMethod("testMethod", Long.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void supportsParameter를_통해_UserId_어노테이션을_붙이지_않은_경우_false를_반환한다() throws Exception {
        // given
        Method method = this.getClass().getMethod("testMethodWithoutAnnotation", Long.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isFalse();
    }


    @Test
    void supportsParameter를_통해_Long_타입이_아닌_경우_false를_반환한다() throws Exception {
        // given
        Method method = this.getClass().getMethod("testMethodWithWrongType", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void resolveArgument를_통해_SecurityContext에_넣어둔_UserId_값을_반환한다() throws Exception {
        // given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(123L);
        SecurityContextHolder.setContext(securityContext);

        Method method = this.getClass().getMethod("testMethod", Long.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        // when
        Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, null);

        // then
        assertThat(result).isEqualTo(123L);
    }
}