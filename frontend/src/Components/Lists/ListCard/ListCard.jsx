import ListIcon from '@mui/icons-material/List';
import { memo, useState } from 'react';
import { useSelector } from 'react-redux/es/hooks/useSelector';
import { useNavigate } from 'react-router-dom';
import ListsModel2 from '../ListsModel2';

const ListCard = memo(({ list }) => {
    const navigate = useNavigate();
    const [openListsModel, setOpenListsModel] = useState();
    const handleCloseListsModel = () => setOpenListsModel(false);
    const handleOpenListsModel = () => setOpenListsModel(true);
    const { theme } = useSelector(store => store);

    return (
        <div class="flex space-x-5">
            <ListIcon
                //onClick={() => navigate(`/lists/${list.id}`)}
                onClick={handleOpenListsModel}
                className="cursor-pointer"
            />
            <div class="w-full">
                <div class="flex justify-between items-center">
                    <div
                        //onClick={() => navigate(`/lists/${list.id}`)}
                        onClick={handleOpenListsModel}
                        className="flex cursor-pointer items-center space-x-2">
                        <span class="text-xl">{list.listName}</span>
                    </div>
                </div>
            </div>

            <section>
                <ListsModel2
                    list={list}
                    open={openListsModel}
                    handleClose={handleCloseListsModel}
                />
            </section>
        </div>
    )
});

export default ListCard;