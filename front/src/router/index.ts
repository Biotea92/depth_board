import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CategoryView from "@/views/CategoryView.vue";
import BoardView from "@/views/BoardView.vue";

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/category',
        name: 'category',
        // component: () => import(/* webpackChunkName: "about" */ '../views/CategoryView.vue')
        component: CategoryView
    },
    {
        path: '/board/:id/:title',
        name: 'board',
        component: BoardView
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
