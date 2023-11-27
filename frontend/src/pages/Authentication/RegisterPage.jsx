import { FormProvider, useForm } from "react-hook-form";
import Input from "./Input";
import RadioButton from "./RadioButton";
import { Link, useNavigate } from "react-router-dom";
import useRegister from "./../../hooks/useRegister";
import { toast } from "react-toastify";
import { useEffect } from "react";

const usernameValidation = {
  minLength: {
    value: 8,
    message: "Tài khoản phải từ 8 kí tự trở lên",
  },
};

const passwordValidation = {
  pattern: {
    value: new RegExp(
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$"
    ),
    message:
      "Mật khẩu phải bao gồm 8 ký tự trở lên, 1 chữ in thường, 1 chữ in hoa, 1 số và 1 ký tự đặc biệt",
  },
};

const emailValidation = {
  required: {
    value: true,
    message: "Email không được trống",
  },
  pattern: {
    value: /\S+@\S+\.\S+/,
    message: "Email không hợp lệ",
  },
};

const fullNameValidation = {
  required: {
    value: true,
    message: "Họ tên không được trống",
  },
};

const addressValidation = {
  required: {
    value: true,
    message: "Địa chỉ không được trống",
  },
};

const phoneNumberValidation = {
  required: {
    value: true,
    message: "Địa chỉ không được trống",
  },
};

function RegisterPage() {
  const methods = useForm();
  const { handleSubmit, watch, setError } = methods;
  const navigate = useNavigate();

  const {
    mutate: register,
    isError: isRegError,
    isLoading: isRegLoading,
    error: regError,
    isSuccess: isRegSuccess,
  } = useRegister();

  const onSubmit = handleSubmit((data) => register(data));

  useEffect(() => {
    if (isRegError) {
      if (regError.response && regError.response.status === 400) {
        setError("username", {
          type: "manual",
          message: "Tài khoản đã tồn tại",
        });
      } else {
        toast.error("Lỗi không xác định");
      }
    }
  }, [isRegError]);

  useEffect(() => {
    if (isRegSuccess) {
      toast.success("Đăng ký thành công, vui lòng đăng nhập");
      navigate("/login");
    }
  }, [isRegSuccess]);

  const passwordConfirmationValidation = {
    required: {
      value: true,
      message: "Vui lòng nhập lại mật khẩu",
    },
    validate: (value) => {
      if (watch("password") !== value) return "Mật khẩu không khớp";

      return true;
    },
  };

  return (
    <div className="mx-auto max-w-sm">
      <FormProvider {...methods}>
        <form onSubmit={onSubmit} noValidate>
          <h2 className="text-liver font-bold text-3xl uppercase mb-4">
            Đăng ký
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
            name="email"
            label="Email"
            placeholder="Nhập email..."
            type="email"
            validation={emailValidation}
          />

          <Input
            className="my-2"
            name="fullName"
            label="Họ tên"
            placeholder="Nhập họ tên..."
            type="text"
            validation={fullNameValidation}
          />

          <Input
            className="my-2"
            name="address"
            label="Địa chỉ"
            placeholder="Nhập địa chỉ..."
            type="text"
            validation={addressValidation}
          />

          <Input
            className="my-2"
            name="phoneNumber"
            label="Số điện thoại"
            placeholder="Nhập số điện thoại.."
            type="text"
            validation={phoneNumberValidation}
          />

          <div className="flex flex-row items-center gap-x-4">
            <RadioButton checked name="gender" value="MALE" label="Nam" />

            <RadioButton name="gender" value="FEMALE" label="Nữ" />
          </div>

          <Input
            className="my-2"
            name="password"
            label="Mật khẩu"
            placeholder="Nhập mật khẩu..."
            type="password"
            validation={passwordValidation}
          />

          <Input
            className="my-2"
            name="passwordConfirmation"
            label="Nhập lại mật khẩu"
            placeholder="Nhập lại mật khẩu..."
            type="password"
            validation={passwordConfirmationValidation}
          />

          <div className="flex flex-row justify-between items-end">
            <button
              disabled={isRegLoading}
              className="bg-liver text-bone px-4 py-2 mt-4 rounded"
            >
              {isRegLoading ? "Đang đăng ký..." : "Đăng ký"}
            </button>

            <Link
              to="/login"
              className="pr-2 pb-2 underline text-liver font-normal text-sm"
            >
              Đăng nhập
            </Link>
          </div>
        </form>
      </FormProvider>
    </div>
  );
}

export default RegisterPage;
