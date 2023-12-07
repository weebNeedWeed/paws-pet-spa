import { Outlet } from "react-router-dom";
import { useLayoutEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useUserContext } from "../../contexts/UserContext";

function AuthLayout() {
  const navigate = useNavigate();
  const [userState] = useUserContext();

  useLayoutEffect(() => {
    if (userState.isAuthenticated) {
      navigate("/");
    }
  }, [userState.isAuthenticated]);

  return (
    <div className="bg-bone min-h-screen">
      <div className="container mx-auto px-4">
        <main className="py-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default AuthLayout;
