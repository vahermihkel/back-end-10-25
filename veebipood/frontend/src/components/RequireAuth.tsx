import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { Navigate, Outlet } from "react-router-dom";

function RequireAuth() {
  const {loggedIn, loading} = useContext(AuthContext);

  if (!loggedIn && !loading) {
    return <Navigate to="/login" />
  }
  
  return ( // jätab originaali alles mille ümber ta on
    <Outlet />
  )
}

export default RequireAuth