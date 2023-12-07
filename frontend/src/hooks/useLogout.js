import { useCallback } from "react";
import { useUserContext } from "../contexts/UserContext";
import { useNavigate } from "react-router-dom";
import useAccessToken from "./useAccessToken";
import { useQueryClient } from "react-query";

function useLogout() {
  const [, , invalidateAccessToken] = useAccessToken();
  const [, userDispatch] = useUserContext();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const handleLogout = useCallback(() => {
    userDispatch({ type: "LOGOUT_USER" });
    invalidateAccessToken();

    queryClient.invalidateQueries("profile");

    navigate("/");
  }, []);

  return handleLogout;
}

export default useLogout;
