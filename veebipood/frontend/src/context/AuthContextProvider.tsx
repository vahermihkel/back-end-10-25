import { type ReactNode, useEffect, useState } from "react";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";

export const AuthContextProvider = ({children}: {children: ReactNode}) => {
  const [loggedIn, setLoggedIn] = useState(false);
  const [person, setPerson] = useState({});
  const [role, setRole] = useState("");
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    if (!sessionStorage.getItem("token")) {
      return;
    }
    fetchPerson(true);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function fetchPerson(refreshing: boolean) {
    fetch(import.meta.env.VITE_BACKEND_URL + "/person", {
      headers: {
        "Authorization": "Bearer " + sessionStorage.getItem("token")
      }
    }).then(res => res.json())
    .then(json => {
      setLoading(false);
      setPerson(json);
      setRole(json.role);
      setLoggedIn(true);
      if (!refreshing) {
        navigate("/profile");
      }
    })
  }

  function login(token: string) {
    sessionStorage.setItem("token", token);
    fetchPerson(false); // asünkroonne päring ehk võtab aega ja senikaua kuni
    // aega võtab, laseb koodil edasi liikuda
    // navigate("/profile");
  }

  function logout() {
    setLoggedIn(false);
    setPerson({});
    setRole("");
    navigate("/");
    sessionStorage.removeItem("token");
  }

  return (
    <AuthContext.Provider value={{loggedIn, person, role, loading, login, logout}}>
      {children}
    </AuthContext.Provider>)
}