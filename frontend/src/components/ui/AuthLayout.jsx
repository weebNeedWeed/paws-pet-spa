import { Outlet } from "react-router-dom";

function AuthLayout() {
  return (
    <div className="bg-bone h-screen">
      <div className="container mx-auto px-4">
        <main className="pt-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default AuthLayout;
