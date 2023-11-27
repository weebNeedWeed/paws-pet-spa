import { useId } from "react";
import PropTypes from "prop-types";
import { useFormContext } from "react-hook-form";

function Checkbox({ label, name }) {
  const id = useId();
  const { register } = useFormContext();

  return (
    <div className="flex items-center">
      <input
        id={id}
        type="checkbox"
        name={name}
        className="w-4 h-4 accent-liver bg-gray-100 border-gray-300 focus:ring-liver focus:ring-2"
        {...register(name)}
      />
      <label htmlFor={id} className="ms-2 text-sm font-medium text-liver">
        {label}
      </label>
    </div>
  );
}

Checkbox.propTypes = {
  label: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
};

export default Checkbox;
