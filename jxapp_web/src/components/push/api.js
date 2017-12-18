import { fetchApi } from '@/utils/index'

// 查询所有就诊排队通知队列
export const QUERY_NOTIFY_URL = '/queue/queryNotifyLength'
export const queryNotifyApi = () => {
  return fetchApi({
    url: QUERY_NOTIFY_URL,
    type: 'get'
  })
}
