type avatarSize = "sm" | "lg";
interface ContactAvatarProps {
  name: string;
  avatarColor: string;
  size: avatarSize;
}

const ContactAvatar = ({ name, avatarColor, size }: ContactAvatarProps) => {
  const firstLetter = name.slice(0, 1).toUpperCase();
  const className = size === "sm" ? "contact-avatar-sm" : "contact-avatar-lg";
  return (
    <div style={{ backgroundColor: avatarColor }} className={className}>
      {firstLetter}
    </div>
  );
};

export default ContactAvatar;
