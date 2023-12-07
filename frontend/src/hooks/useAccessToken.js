import { useCallback, useState } from "react";

const token_key = "token";

export default function useAccessToken() {
  const [accessToken, setAccessToken] = useState(() => {
    const localStorageToken = localStorage.getItem(token_key);
    const sessionStorageToken = sessionStorage.getItem(token_key);
    if (localStorageToken && sessionStorageToken) {
      return "";
    }

    const token = localStorageToken || sessionStorageToken;
    return token || "";
  });

  const setAccessTokenWithRememberMeMode = useCallback((token, rememberMe) => {
    if (rememberMe) {
      localStorage.setItem(token_key, token);
    } else {
      sessionStorage.setItem(token_key, token);
    }

    setAccessToken(token);
  }, []);

  const invalidateAccessToken = useCallback(() => {
    localStorage.removeItem(token_key);
    sessionStorage.removeItem(token_key);

    setAccessToken("");
  }, []);

  return [accessToken, setAccessTokenWithRememberMeMode, invalidateAccessToken];
}
