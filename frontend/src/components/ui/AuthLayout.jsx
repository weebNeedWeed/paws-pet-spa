import { Outlet } from "react-router-dom";
import "react-toastify/dist/ReactToastify.css";

function AuthLayout() {
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
