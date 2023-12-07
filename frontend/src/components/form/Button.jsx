import clsx from "clsx";
import PropTypes from "prop-types";

function Button(props) {
  const className = clsx(
    "bg-desaturatedCyan text-bone px-4 py-2 rounded w-fit flex flex-row items-center",
    props.className
  );

  return <button {...props} className={className} />;
}

Button.propTypes = {
  className: PropTypes.string,
};

export default Button;
