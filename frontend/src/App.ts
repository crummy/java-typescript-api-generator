import { UsersApi } from "./api/services.js"

const usersApi = new UsersApi();

function init() {
    const body = document.querySelector("body")!!
    const header: HTMLHeadingElement = body.querySelector(".header")!!
    header.innerHTML = "Loading..."
    const usersList: HTMLUListElement = body.querySelector(".users-list")!!
    usersApi.getUsers().then(users => {
        header.innerHTML = `${users.length} Users`
        usersList.innerHTML = users.map(u => `<li>${u.userId}: ${u.username}</li>`).join("\n")
    })
}

init()