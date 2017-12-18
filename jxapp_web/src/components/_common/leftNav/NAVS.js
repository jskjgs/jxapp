import {
  INDEX as bannerCfg
} from '@/components/banner/_consts/routers'
import {
  INDEX as logCfg
} from '@/components/diary/_consts/routers'
import {
  INDEX as reserveCfg
} from '@/components/reserve/_consts/routers'
import {
  INDEX as doctorCfg
} from '@/components/doctor/_consts/routers'
import {
  INDEX as userCfg
} from '@/components/user/_consts/routers'
import {
  INDEX as authCfg
} from '@/components/auth/_consts/routers'
import {
  INDEX as pushCfg
} from '@/components/push/_consts/routers'

export default [{
  label: '运营管理',
  icon: 'icon-operate',
  children: [{
    label: bannerCfg.meta.label,
    path: bannerCfg.path,
    permissionId: bannerCfg.meta.permissionId
  }, {
    label: logCfg.meta.label,
    path: logCfg.path,
    permissionId: logCfg.meta.permissionId
  }]
}, {
  label: reserveCfg.meta.label,
  icon: 'icon-reservation',
  path: reserveCfg.path,
  permissionId: reserveCfg.meta.permissionId
}, {
  label: doctorCfg.meta.label,
  icon: 'icon-doctor',
  path: doctorCfg.path,
  permissionId: doctorCfg.meta.permissionId
}, {
  label: userCfg.meta.label,
  icon: 'icon-user',
  path: userCfg.path,
  permissionId: userCfg.meta.permissionId
}, {
  label: authCfg.meta.label,
  icon: 'icon-access',
  path: authCfg.path,
  permissionId: authCfg.meta.permissionId
}, {
  label: pushCfg.meta.label,
  icon: 'icon-push',
  path: pushCfg.path,
  permissionId: pushCfg.meta.permissionId
}]
