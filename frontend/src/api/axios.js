import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
})

// 요청 인터셉터: JWT 토큰 자동 첨부
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 응답 인터셉터: 401 시 로그아웃 처리
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('member')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 로그인 (x-www-form-urlencoded)
export const login = async (username, password) => {
  const params = new URLSearchParams()
  params.append('username', username)
  params.append('password', password)

  const response = await api.post('/login', params, {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
  })

  const token = response.headers['authorization']?.replace('Bearer ', '')
  return { token, member: response.data }
}

// 회원
export const signUp           = (data) => api.post('/members', data)
export const checkId          = (id) => api.get(`/members/check?id=${id}`)
export const getMyInfo        = () => api.get('/members/my')
export const updateMyInfo     = (data) => api.put('/members/my', data)
export const deleteMyAccount  = () => api.delete('/members/my')

// 관리자
export const adminGetMembers      = () => api.get('/admin/members')
export const adminDeleteMember    = (memberNo) => api.delete(`/admin/members/${memberNo}`)

// 파일 업로드
export const uploadImage     = (file) => { const f = new FormData(); f.append('file', file); return api.post('/admin/upload/image', f) }
export const uploadUserImage = (file) => { const f = new FormData(); f.append('file', file); return api.post('/upload/image', f) }

// 장소
export const getPlaces     = () => api.get('/places')
export const searchPlaces  = (keyword) => api.get(`/places/search?keyword=${keyword}`)
export const getPlace      = (id) => api.get(`/places/${id}`)
export const addPlace      = (data) => api.post('/places', data)
export const updatePlace   = (id, data) => api.put(`/places/${id}`, data)
export const deletePlace   = (id) => api.delete(`/places/${id}`)

// 게시글
export const getBoards     = (page = 0, size = 10) => api.get(`/boards?page=${page}&size=${size}`)
export const getMyBoards   = () => api.get('/boards/my')
export const getBoard      = (id) => api.get(`/boards/${id}`)
export const searchBoards  = (keyword) => api.get(`/boards/search?keyword=${keyword}`)
export const getBoardsByPlace = (placeId) => api.get(`/boards/place/${placeId}`)
export const addBoard      = (data) => api.post('/boards', data)
export const updateBoard   = (id, data) => api.put(`/boards/${id}`, data)
export const deleteBoard   = (id) => api.delete(`/boards/${id}`)

// 데이트 신청
export const sendRequest   = (boardId, data) => api.post(`/boards/${boardId}/date-requests`, data)
export const getRequestsByBoard = (boardId) => api.get(`/boards/${boardId}/date-requests`)
export const getReceivedRequests = () => api.get('/date-requests/received')
export const getSentRequests     = () => api.get('/date-requests/sent')
export const acceptRequest = (id) => api.put(`/date-requests/${id}/accept`)
export const rejectRequest = (id) => api.put(`/date-requests/${id}/reject`)
export const cancelRequest = (id) => api.delete(`/date-requests/${id}`)

export default api
