import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import '@mdi/font/css/materialdesignicons.css'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import {createPinia, Pinia} from 'pinia'

const vuetify = createVuetify({
    components,
    directives,
})

const pinia = createPinia();

createApp(App)
    .use(vuetify)
    .use(router)
    .use(pinia)
    .mount('#app')