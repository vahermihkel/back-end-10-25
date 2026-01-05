import { createContext } from "react";

export const AuthContext = createContext({
  loggedIn: false,
  person: {},
  role: "",
  loading: false,
  login: (_token: string) => {console.log(_token)},
  logout: () => {}
})