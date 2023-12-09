import { useEffect } from "react";
import { useUserContext } from "./../contexts/UserContext";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

function useAuthenticatedRoute() {
  const [userState] = useUserContext();
  const { isAuthenticated } = userState;
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      toast.warning("Bạn cần đăng nhập để thực hiện tính năng này");
      navigate("/login");
    }
  }, [isAuthenticated]);
}

export default useAuthenticatedRoute;
