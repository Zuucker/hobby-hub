export const setCookie = (name: string, value: string) => {
  const date = new Date();
  const timeToExpire = date.getTime() + 30 * 60 * 1000;
  const cookie: string =
    name + "=" + value + ";" + timeToExpire + ";" + "path=/";
  console.log(cookie);
  document.cookie = cookie;
};
export const readCookie = (name: string) => {
  const cookies: string[] = document.cookie.split(";");

  let myCookie = cookies.find((c) => c.includes(name));
  myCookie = myCookie?.replaceAll(name, "");
  myCookie = myCookie?.replace("=", "");

  return myCookie;
};
