import { FormProvider, useForm } from "react-hook-form";
import Input from "./Input";

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
  const methods = useForm();
  const { handleSubmit } = methods;

  const onSubmit = handleSubmit(console.log);

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
            className="mt-2"
            name="password"
            label="Mật khẩu"
            placeholder="Nhập mật khẩu..."
            type="password"
            validation={passwordValidation}
          />

          <button className="bg-liver text-bone px-4 py-2 mt-4 rounded">
            Đăng nhập
          </button>
        </form>
      </FormProvider>
    </div>
  );
}

export default LoginPage;
