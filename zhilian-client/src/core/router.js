import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/auth',
    children: [
      {
        path: 'login',
        name: 'Login',
        component: () => import('../views/auth/Login.vue')
      },
      {
        path: 'register',
        name: 'Register',
        component: () => import('../views/auth/Register.vue')
      },
      {
        path: 'reset-password',
        name: 'ResetPassword',
        component: () => import('../views/auth/ResetPassword.vue')
      }
    ]
  },
  {
    path: '/user',
    children: [
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/user/Profile.vue')
      }
    ]
  },
  {
    path: '/buyer',
    children: [
      {
        path: 'list',
        name: 'BuyerList',
        component: () => import('../views/buyer/List.vue')
      }
    ]
  },
  {
    path: '/order',
    children: [
      {
        path: 'list',
        name: 'OrderList',
        component: () => import('../views/order/List.vue')
      },
      {
        path: 'detail/:id',
        name: 'OrderDetail',
        component: () => import('../views/order/Detail.vue')
      },
      {
        path: 'create',
        name: 'OrderCreate',
        component: () => import('../views/order/Create.vue')
      }
    ]
  },
  {
    path: '/payment',
    children: [
      {
        path: 'pay/:id',
        name: 'Pay',
        component: () => import('../views/payment/Pay.vue')
      }
    ]
  },
  {
    path: '/image',
    children: [
      {
        path: 'upload',
        name: 'ImageUpload',
        component: () => import('../views/image/Upload.vue')
      }
    ]
  },
  {
    path: '/base',
    children: [
      {
        path: 'region',
        name: 'Region',
        component: () => import('../views/base/Region.vue')
      },
      {
        path: 'channel',
        name: 'Channel',
        component: () => import('../views/base/Channel.vue')
      }
    ]
  },
  {
    path: '/category',
    children: [
      {
        path: 'list',
        name: 'CategoryList',
        component: () => import('../views/category/List.vue')
      }
    ]
  },
  {
    path: '/event',
    children: [
      {
        path: 'detail/:id',
        name: 'EventDetail',
        component: () => import('../views/event/Detail.vue')
      }
    ]
  },
  {
    path: '/ai',
    children: [
      {
        path: 'assistant',
        name: 'Assistant',
        component: () => import('../views/ai/Assistant.vue')
      }
    ]
  },
  {
    path: '/test',
    children: [
      {
        path: 'token',
        name: 'TokenTest',
        component: () => import('../views/test/TokenTest.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router