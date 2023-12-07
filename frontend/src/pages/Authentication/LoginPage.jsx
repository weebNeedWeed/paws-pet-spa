import { FormProvider, useForm } from "react-hook-form";
import { Input, Checkbox } from "../../components/form";
import useLogin from "../../hooks/useLogin";
import { toast } from "react-toastify";
import useAccessToken from "../../hooks/useAccessToken";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

const usernameValidation = {
  required: {
    value: true,
    message: "Tài khoản không được trống",
  },
};

const passwordValidation = {
  required: {
    value: true,
    message: "Mật khẩu không được trống",
  },
};

function LoginPage() {
  const [rememberMe, setRememberMe] = useState(false);
  const methods = useForm();
  const { handleSubmit } = methods;

  const [, setAccessToken] = useAccessToken();
  const navigate = useNavigate();

  const {
    mutate: login,
    isError: isLoginError,
    isLoading: isLoginLoading,
    data: loginData,
    error: loginError,
    isSuccess: isLoginSuccess,
  } = useLogin();

  const onSubmit = handleSubmit((data) => {
    const { username, password } = data;

    login({ username, password });
    setRememberMe(data.rememberMe);
  });

  useEffect(() => {
    if (isLoginError) {
      if (loginError.response && loginError.response.status === 401) {
        toast.error("Sai tài khoản hoặc mật khẩu");
      } else {
        toast.error("Lỗi không xác định");
      }
    }
  }, [isLoginError]);

  useEffect(() => {
    if (isLoginSuccess) {
      const { token: accessToken } = loginData.data;
      setAccessToken(accessToken, rememberMe);

      toast.success("Đăng nhập thành công");
      navigate("/");
    }
  }, [isLoginSuccess]);

  return (
    <div className="mx-auto max-w-sm">
      <FormProvider {...methods}>
        <form onSubmit={onSubmit} noValidate>
          <h2 className="text-liver font-bold text-3xl uppercase mb-4">
            Đăng nhập
          </h2>

          <Input
            name="username"
            label="Tài khoản"
            placeholder="Nhập tài khoản..."
            type="text"
            validation={usernameValidation}
          />

          <Input
            className="my-2"
            name="password"
            label="Mật khẩu"
            placeholder="Nhập mật khẩu..."
            type="password"
            validation={passwordValidation}
          />

          <Checkbox name="rememberMe" label="Nhớ mật khẩu" />

          <div className="flex flex-row justify-between items-end">
            <button
              disabled={isLoginLoading}
              className="bg-liver text-bone px-4 py-2 mt-4 rounded"
            >
              {isLoginLoading ? "Đang đăng nhập..." : "Đăng nhập"}
            </button>

            <Link
              to="/register"
              className="pr-2 pb-2 underline text-liver font-normal text-sm"
            >
              Đăng ký
            </Link>
          </div>
        </form>
      </FormProvider>
    </div>
  );
}

export default LoginPage;
