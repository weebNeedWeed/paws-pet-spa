import logo from "./../../assets/logo.svg";
import { Link } from "react-router-dom";

function NavBar() {
  return (
    <nav className="text-lg font-medium flex flex-row justify-between items-center py-8">
      <div className="flex items-center gap-x-8">
        <Link to="/">
          <img src={logo} alt="paws logo" className="w-28" />
        </Link>

        <ul className="list-none flex flex-row gap-x-4 text-liver">
          <li>
            <Link to="/">Trang chủ</Link>
          </li>
        </ul>
      </div>

      <div className="flex items-center gap-x-6">
        <Link className="text-liver">Đăng ký</Link>

        <Link
          to={"/login"}
          className="bg-desaturatedCyan text-bone px-4 py-2 rounded-md"
        >
          Đăng nhập
        </Link>
      </div>
    </nav>
  );
}

export default NavBar;
