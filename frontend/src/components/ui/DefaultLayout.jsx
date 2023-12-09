import { Outlet, useNavigate } from "react-router-dom";
import { NavBar, Footer } from "./";
import { useGetUserProfile, useLogout, useAccessToken } from "../../hooks";
import { useUserContext } from "../../contexts/UserContext";
import { useEffect } from "react";
import { toast } from "react-toastify";

function DefaultLayout() {
  const [accessToken] = useAccessToken();
  const {
    isError: isProfileError,
    data: profile,
    isSuccess: isProfileSuccess,
    refetch: refetchProfile,
  } = useGetUserProfile();
  const [, userDispatch] = useUserContext();
  const logout = useLogout();
  const navigate = useNavigate();

  useEffect(() => {
    if (accessToken) refetchProfile();
  }, [accessToken]);

  useEffect(() => {
    if (isProfileError && accessToken) {
      logout();
      toast.info("Phiên hết hạn, vui lòng đăng nhập lại");
      navigate("/login");
    }
  }, [isProfileError]);

  useEffect(() => {
    if (isProfileSuccess && accessToken) {
      const username = profile.data.username;
      userDispatch({ type: "AUTH_USER", payload: { username } });
    }
  }, [isProfileSuccess]);

  return (
    <div className="bg-bone min-h-screen">
      <div className="container mx-auto px-4">
        <header>
          <NavBar />
        </header>

        <Outlet />
      </div>

      <Footer />
    </div>
  );
}

export default DefaultLayout;
