import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";
import { viewPlus } from "../../../Store/Tweet/Action";
import { decreaseNotificationCount } from "../../../Store/Notification/Action";
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';
import { Avatar } from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";

const NotificationsCard = ({ notification }) => {
    const { auth } = useSelector((store) => store);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleNavigateToTwitDetial = () => {
          navigate(`/twit/${notification.twit.id}`);
          dispatch(viewPlus(notification.twit.id));
          window.location.reload();
          dispatch(decreaseNotificationCount(notification.id));
      };
    return (
        <div class="flex space-x-5">
            <div
                onClick={handleNavigateToTwitDetial}
                className="cursor-pointer"
            />
            <div class="w-full">
                <div class="flex justify-between items-center">
                    <div
                        onClick={handleNavigateToTwitDetial}
                        className="flex cursor-pointer items-center space-x-1"
                    >
                        <li style={{listStyleType: 'none'}} className="list-css"><Avatar
                        alt="Avatar"
                        src={
                            notification.user?.image
                              ? notification.user.image
                              : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"
                          }
                          loading="lazy"
                          style={{float:"left"}}
                        /><span style={{padding:"5px",float:"left"}}>당신의 리빗을</span><span style={{padding:"5px",float:"left"}}>{notification.user.fullName}이(가) 좋아합니다.</span></li>
                    </div>
                </div>
                <hr
            style={{
              marginTop: 10,
              marginBottom: 1,
              background: 'grey',
              color: 'grey',
              borderColor: 'grey',
              height: '1px',
            }}
          />
            </div>
        </div>
    );
};

export default NotificationsCard;