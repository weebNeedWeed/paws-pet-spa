import { Outlet } from "react-router-dom";
import NavBar from "./NavBar";

function DefaultLayout() {
  return (
    <div className="bg-bone min-h-screen">
      <div className="container mx-auto px-4">
        <header>
          <NavBar />
        </header>

        <Outlet />
      </div>
    </div>
  );
}

export default DefaultLayout;
