import logo from "./../../assets/logo.svg";
import { Link } from "react-router-dom";
import { useUserContext } from "../../contexts/UserContext";
import { useLogout } from "./../../hooks";
import { toast } from "react-toastify";

function NavBar() {
  const [userState] = useUserContext();
  const { isAuthenticated, username } = userState;

  const logout = useLogout();
  const handleLogout = () => {
    logout();
    toast.success("Đăng xuất thành công");
  };

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

          <li>
            <Link to="/booking">Đặt hẹn</Link>
          </li>
        </ul>
      </div>

      <div className="flex items-center gap-x-6">
        {isAuthenticated ? (
          <>
            <div>
              Xin chào, <span className="underline">{username}</span>
            </div>

            <button onClick={handleLogout} className="font-bold">
              Đăng xuất
            </button>
          </>
        ) : (
          <>
            <Link to={"/register"} className="text-liver">
              Đăng ký
            </Link>

            <Link
              to={"/login"}
              className="bg-desaturatedCyan text-bone px-4 py-2 rounded-md"
            >
              Đăng nhập
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default NavBar;
