import PropTypes from "prop-types";
import { useId } from "react";
import { useFormContext } from "react-hook-form";

function RadioButton({ name, label, value, checked }) {
  const { register } = useFormContext();
  const id = useId();

  return (
    <div className="flex items-center">
      <input
        {...register(name)}
        defaultChecked={checked}
        id={id}
        value={value}
        type="radio"
        className="w-4 h-4 accent-liver bg-gray-100 border-gray-300 rounded"
      />
      <label htmlFor={id} className="ms-2 text-base font-medium text-liver">
        {label}
      </label>
    </div>
  );
}

RadioButton.propTypes = {
  name: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  checked: PropTypes.bool,
};

export default RadioButton;
