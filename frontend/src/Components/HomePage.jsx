import { Grid } from "@mui/material";
import React, { Suspense, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Route, Routes } from "react-router-dom";
import HomeSection from "./Home/MiddlePart/HomeSection";
import Navigation from "./Navigation/Navigation";
import Loading from "./Profile/Loading/Loading";
import RightPart from "./RightPart/RightPart";

const FollowTwitEnc = React.lazy(() => import("./FollowTwit/FollowTwitEnc"));
const TwitDetail = React.lazy(() => import("./Home/MiddlePart/TwitDetail"));
const Lists = React.lazy(() => import("./Lists/Lists"));
const Profile = React.lazy(() => import("./Profile/Profile"));
const Chatroom = React.lazy(() => import("./Chat/Chat1"));
const ListsDetail = React.lazy(() => import("./Lists/ListsDetail"));
const Communities = React.lazy(() => import("./Communities/Communities"));
const ComDetail = React.lazy(() => import("./Communities/ComDetail"));
const NotificationsPage = React.lazy(() => import("./Notifications/NotificationsPage"));

const HomePage = () => {
  const { theme } = useSelector((store) => store);
  const [uploading, setUploading] = useState(false);
  const [sendTheme, setSendTheme] = useState(theme);
  const [sendRefreshPage, setSendRefreshPage] = useState(0);

  const changeThemeAllFunc = (newTheme) => {
    setSendTheme(newTheme);
  }

  const changePageFunc = () => {
    setSendRefreshPage((prev) => prev + 1);
  }

  return (
    <Grid container className="px-5 lg:px-36 justify-between" xs={12}>
      <Grid item xs={0} lg={2.5} className="hidden lg:block  w-full relative">
          <Navigation />
      </Grid>
      <Grid
        item
        xs={12}
        lg={6}
        className={`px-5 lg:px-9 border`}
      >
        <Routes>
          <Route path="/" element={
              <HomeSection sendRefreshPage= {sendRefreshPage} changePage={changePageFunc}/>
          }></Route>
          <Route path="/profile/:id" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <Profile changePage={changePageFunc} sendRefreshPage= {sendRefreshPage}/>
            </Suspense>}></Route>
          <Route path="/followtwit" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <FollowTwitEnc changePage={changePageFunc} sendRefreshPage= {sendRefreshPage}/>
            </Suspense>}></Route>
          <Route path="/messages" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <Chatroom />
            </Suspense>}></Route>
          <Route path="/lists" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <Lists />
            </Suspense>}></Route>
          <Route path="/communities" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <Communities changePage={changePageFunc} sendRefreshPage= {sendRefreshPage}/>
            </Suspense>}></Route>
          <Route path="/twit/:id" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <TwitDetail changePage={changePageFunc} sendRefreshPage= {sendRefreshPage}/>
            </Suspense>}></Route>
          <Route path="/lists/:id" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <ListsDetail changePage={changePageFunc} sendRefreshPage= {sendRefreshPage}/>
            </Suspense>}></Route>
            <Route path="/communities/:id" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <ComDetail changePage={changePageFunc} sendRefreshPage= {sendRefreshPage}/>
            </Suspense>}></Route>
            <Route path="/notifications" element={
            <Suspense fallback={<div> {uploading ? <Loading/> : null}  </div>}>
              <NotificationsPage />
            </Suspense>}></Route>
        </Routes>
      </Grid>
      <Grid item xs={0} lg={3} className="hidden lg:block">
        <RightPart 
          changeThemeAll={changeThemeAllFunc} sendRefreshPage={sendRefreshPage} changePage={changePageFunc} sendTheme={sendTheme}/>
      </Grid>
    </Grid>
  );
};

export default HomePage;
