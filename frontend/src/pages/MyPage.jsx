import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import {
  getReceivedRequests, getSentRequests,
  acceptRequest, rejectRequest, cancelRequest,
  getMyInfo, updateMyInfo, deleteMyAccount, uploadUserImage, getMyBoards,
} from '../api/axios'
import { useAuth } from '../contexts/AuthContext'
import ImageUploader from '../components/ImageUploader'

const tabs = [
  { key: 'received', label: '받은 신청' },
  { key: 'sent',     label: '보낸 신청' },
  { key: 'boards',   label: '내 게시글' },
  { key: 'profile',  label: '내 프로필' },
]

export default function MyPage() {
  const { member, logout } = useAuth()
  const navigate = useNavigate()
  const [activeTab, setActiveTab] = useState('received')
  const [received, setReceived] = useState([])
  const [sent, setSent] = useState([])
  const [loading, setLoading] = useState(true)
  const [toast, setToast] = useState('')

  const showToast = (msg) => { setToast(msg); setTimeout(() => setToast(''), 3000) }

  const fetchAll = async () => {
    setLoading(true)
    try {
      const [r, s] = await Promise.all([getReceivedRequests(), getSentRequests()])
      setReceived(r.data)
      setSent(s.data)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchAll() }, [])

  const handleAccept = async (id) => { await acceptRequest(id); showToast('수락했습니다'); fetchAll() }
  const handleReject = async (id) => { await rejectRequest(id); showToast('거절했습니다'); fetchAll() }
  const handleCancel = async (id) => {
    if (!window.confirm('신청을 취소할까요?')) return
    await cancelRequest(id); showToast('신청을 취소했습니다'); fetchAll()
  }
  const handleLogout = () => { logout(); navigate('/') }

  const pendingCount = received.filter(r => r.status === 'PENDING').length

  return (
    <div className="page">
      {/* Profile header */}
      <div style={{ background: '#0a0a0a', padding: '100px 0 80px' }}>
        <div className="container">
          <p style={{
            fontSize: '11px', fontWeight: '700', letterSpacing: '4px',
            color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', marginBottom: '20px',
          }}>
            My Account
          </p>
          <div style={{ display: 'flex', alignItems: 'flex-end', justifyContent: 'space-between', flexWrap: 'wrap', gap: '24px' }}>
            <div>
              <h1 style={{ fontSize: 'clamp(32px, 5vw, 56px)', fontWeight: '800', color: '#fff', letterSpacing: '-1px', lineHeight: 1.05 }}>
                {member?.name}
              </h1>
              <p style={{ fontSize: '15px', color: 'rgba(255,255,255,0.4)', marginTop: '12px', letterSpacing: '0.3px' }}>
                @{member?.id}
                {member?.role === 'ROLE_ADMIN' && (
                  <span style={{
                    marginLeft: '12px', fontSize: '10px', fontWeight: '700', letterSpacing: '2px',
                    background: 'rgba(255,255,255,0.1)', color: 'rgba(255,255,255,0.6)',
                    padding: '3px 10px', textTransform: 'uppercase',
                  }}>ADMIN</span>
                )}
              </p>
            </div>
            <button onClick={handleLogout} className="btn btn-outline-white" style={{ fontSize: '12px' }}>
              로그아웃
            </button>
          </div>
        </div>
      </div>

      <div className="container" style={{ padding: '0 40px' }}>
        {/* Tabs */}
        <div style={{ display: 'flex', borderBottom: '1px solid #e8e8e8' }}>
          {tabs.map(({ key, label }) => (
            <button key={key} onClick={() => setActiveTab(key)} style={{
              padding: '24px 0', marginRight: '40px',
              background: 'none', border: 'none',
              fontSize: '14px', fontWeight: '700', letterSpacing: '0.5px',
              color: activeTab === key ? '#0a0a0a' : '#c0c0c0',
              borderBottom: activeTab === key ? '2px solid #0a0a0a' : '2px solid transparent',
              marginBottom: '-1px', transition: 'all 0.2s', cursor: 'pointer',
            }}>
              {label}
              {key === 'received' && pendingCount > 0 && (
                <span style={{
                  marginLeft: '8px', background: '#0a0a0a', color: '#fff',
                  borderRadius: '2px', padding: '2px 7px', fontSize: '11px',
                }}>
                  {pendingCount}
                </span>
              )}
            </button>
          ))}
        </div>

        {/* Content */}
        <div style={{ padding: '64px 0' }}>
          {activeTab === 'received' && (
            loading ? <div className="loading">불러오는 중</div>
              : <RequestList requests={received} isReceived onAccept={handleAccept} onReject={handleReject} />
          )}
          {activeTab === 'sent' && (
            loading ? <div className="loading">불러오는 중</div>
              : <RequestList requests={sent} onCancel={handleCancel} />
          )}
          {activeTab === 'boards' && <MyBoardsTab />}
          {activeTab === 'profile' && (
            <ProfileTab showToast={showToast} onLogout={handleLogout} />
          )}
        </div>
      </div>

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
}

/* ── 내 게시글 탭 ── */
function MyBoardsTab() {
  const [boards, setBoards] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getMyBoards()
      .then(r => setBoards(r.data))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="loading">불러오는 중</div>
  if (boards.length === 0) return (
    <div className="empty">
      <div className="empty-icon">📝</div>
      <p>작성한 게시글이 없어요</p>
    </div>
  )

  return (
    <div>
      <div style={{
        display: 'grid', gridTemplateColumns: '1fr 140px 80px',
        padding: '12px 0', borderBottom: '2px solid #0a0a0a',
        fontSize: '11px', fontWeight: '700', letterSpacing: '1.5px',
        textTransform: 'uppercase', color: '#a0a0a0',
      }}>
        <span>제목</span>
        <span>장소</span>
        <span style={{ textAlign: 'right' }}>조회</span>
      </div>
      {boards.map((board, i) => (
        <div key={board.boardId}>
          <Link to={`/boards/${board.boardId}`} style={{
            display: 'grid', gridTemplateColumns: '1fr 140px 80px',
            alignItems: 'center', padding: '24px 0', gap: '24px',
            transition: 'opacity 0.2s',
          }}
            onMouseEnter={e => e.currentTarget.style.opacity = '0.5'}
            onMouseLeave={e => e.currentTarget.style.opacity = '1'}
          >
            <div>
              <p style={{ fontSize: '16px', fontWeight: '600', letterSpacing: '-0.2px', marginBottom: '4px' }}>
                {board.title}
              </p>
              <p style={{ fontSize: '13px', color: '#a0a0a0' }}>
                {board.regDate?.slice(0, 10)}
              </p>
            </div>
            <span style={{ fontSize: '13px', color: '#a0a0a0' }}>
              {board.place?.placeName || '—'}
            </span>
            <span style={{ fontSize: '13px', color: '#a0a0a0', textAlign: 'right' }}>
              {board.viewCount || 0}
            </span>
          </Link>
          {i < boards.length - 1 && <div className="divider" />}
        </div>
      ))}
    </div>
  )
}

/* ── 프로필 탭 ── */
function ProfileTab({ showToast, onLogout }) {
  const [info, setInfo] = useState(null)
  const [editing, setEditing] = useState(false)
  const [form, setForm] = useState({})
  const [saving, setSaving] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    getMyInfo().then(r => {
      setInfo(r.data)
      setForm({
        name: r.data.name || '',
        email: r.data.email || '',
        gender: r.data.gender || '',
        age: r.data.age || '',
        introduce: r.data.introduce || '',
        profileImg: r.data.profileImg || '',
      })
    })
  }, [])

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      const res = await updateMyInfo({ ...form, age: form.age ? Number(form.age) : null })
      setInfo(res.data)
      setEditing(false)
      showToast('프로필을 수정했습니다')
    } catch {
      showToast('수정에 실패했습니다')
    } finally {
      setSaving(false)
    }
  }

  const handleWithdraw = async () => {
    if (!window.confirm('정말 탈퇴하시겠어요? 모든 데이터가 삭제되며 복구할 수 없습니다.')) return
    try {
      await deleteMyAccount()
      onLogout()
      navigate('/')
    } catch {
      showToast('탈퇴에 실패했습니다')
    }
  }

  if (!info) return <div className="loading">불러오는 중</div>

  const fields = [
    { label: '아이디', value: info.id },
    { label: '이름', value: info.name },
    { label: '이메일', value: info.email || '—' },
    { label: '성별', value: info.gender === 'MALE' ? '남성' : info.gender === 'FEMALE' ? '여성' : info.gender || '—' },
    { label: '나이', value: info.age ? `${info.age}세` : '—' },
    { label: '소개', value: info.introduce || '—' },
  ]

  return (
    <div style={{ maxWidth: '560px' }}>
      {!editing ? (
        <>
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginBottom: '40px' }}>
            {info.profileImg ? (
              <img src={info.profileImg} alt="" style={{ width: '120px', height: '120px', borderRadius: '50%', objectFit: 'cover', border: '2px solid #e8e8e8', flexShrink: 0 }} />
            ) : (
              <div style={{ width: '120px', height: '120px', borderRadius: '50%', background: '#0a0a0a', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                <span style={{ fontSize: '40px', color: '#fff', fontWeight: '700' }}>{info.name?.[0]}</span>
              </div>
            )}
            <div>
              <p style={{ fontSize: '18px', fontWeight: '700', color: '#0a0a0a' }}>{info.name}</p>
              <p style={{ fontSize: '13px', color: '#a0a0a0', marginTop: '2px' }}>@{info.id}</p>
            </div>
          </div>

          <div style={{ borderTop: '2px solid #0a0a0a', marginBottom: '48px' }}>
            {fields.map(({ label, value }) => (
              <div key={label} style={{
                display: 'grid', gridTemplateColumns: '120px 1fr',
                padding: '20px 0', borderBottom: '1px solid #e8e8e8',
              }}>
                <span style={{ fontSize: '11px', fontWeight: '700', letterSpacing: '1.5px', textTransform: 'uppercase', color: '#a0a0a0', paddingTop: '2px' }}>
                  {label}
                </span>
                <span style={{ fontSize: '15px', color: '#0a0a0a', lineHeight: 1.5 }}>{value}</span>
              </div>
            ))}
          </div>
          <div style={{ display: 'flex', gap: '12px' }}>
            <button onClick={() => setEditing(true)} className="btn btn-dark" style={{ fontSize: '12px' }}>
              정보 수정
            </button>
            <button onClick={handleWithdraw} className="btn btn-outline-dark"
              style={{ fontSize: '12px', color: '#c8102e', borderColor: '#c8102e' }}>
              회원 탈퇴
            </button>
          </div>
        </>
      ) : (
        <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '28px' }}>
          <div className="form-group">
            <label>프로필 사진</label>
            <ImageUploader
              currentImg={form.profileImg}
              onUploaded={(url) => setForm(p => ({ ...p, profileImg: url }))}
              uploadFn={uploadUserImage}
            />
          </div>
          <div className="form-group">
            <label>이름</label>
            <input value={form.name} onChange={e => setForm(p => ({ ...p, name: e.target.value }))} required />
          </div>
          <div className="form-group">
            <label>이메일</label>
            <input type="email" value={form.email} onChange={e => setForm(p => ({ ...p, email: e.target.value }))} />
          </div>
          <div className="form-group">
            <label>성별</label>
            <select value={form.gender} onChange={e => setForm(p => ({ ...p, gender: e.target.value }))}>
              <option value="">선택 안 함</option>
              <option value="MALE">남성</option>
              <option value="FEMALE">여성</option>
              <option value="OTHER">기타</option>
            </select>
          </div>
          <div className="form-group">
            <label>나이</label>
            <input type="number" min="1" max="100" value={form.age} onChange={e => setForm(p => ({ ...p, age: e.target.value }))} />
          </div>
          <div className="form-group">
            <label>소개</label>
            <textarea value={form.introduce} onChange={e => setForm(p => ({ ...p, introduce: e.target.value }))}
              placeholder="간단한 자기소개를 입력하세요" style={{ minHeight: '100px' }} />
          </div>
          <div style={{ display: 'flex', gap: '12px' }}>
            <button type="submit" disabled={saving} className="btn btn-dark" style={{ fontSize: '12px' }}>
              {saving ? '저장 중...' : '저장'}
            </button>
            <button type="button" onClick={() => setEditing(false)} className="btn btn-outline-dark" style={{ fontSize: '12px' }}>
              취소
            </button>
          </div>
        </form>
      )}
    </div>
  )
}

/* ── 신청 목록 ── */
function RequestList({ requests, isReceived, onAccept, onReject, onCancel }) {
  const [profileTarget, setProfileTarget] = useState(null)

  if (requests.length === 0) return (
    <div className="empty">
      <div className="empty-icon">{isReceived ? '📭' : '📤'}</div>
      <p>{isReceived ? '받은 신청이 없어요' : '보낸 신청이 없어요'}</p>
    </div>
  )

  const statusMap = { PENDING: '대기중', ACCEPTED: '수락됨', REJECTED: '거절됨' }
  const badgeMap  = { PENDING: 'badge-pending', ACCEPTED: 'badge-accepted', REJECTED: 'badge-rejected' }

  return (
    <>
      <div>
        <div style={{
          display: 'grid', gridTemplateColumns: '1fr 300px 100px 160px',
          padding: '12px 0', borderBottom: '2px solid #0a0a0a',
          fontSize: '11px', fontWeight: '700', letterSpacing: '1.5px',
          textTransform: 'uppercase', color: '#a0a0a0',
        }}>
          <span>메시지</span>
          <span>{isReceived ? '신청자' : '게시글'}</span>
          <span>상태</span>
          <span style={{ textAlign: 'right' }}>액션</span>
        </div>

        {requests.map((req, i) => (
          <div key={req.requestId}>
            <div style={{
              display: 'grid', gridTemplateColumns: '1fr 300px 100px 160px',
              alignItems: 'center', padding: '28px 0', gap: '24px',
            }}>
              <div>
                <p style={{ fontSize: '15px', fontWeight: '500', lineHeight: 1.5, color: '#1a1a1a' }}>
                  "{req.message}"
                </p>
                <p style={{ fontSize: '12px', color: '#c0c0c0', marginTop: '6px' }}>
                  {req.regDate?.slice(0, 10)}
                </p>
              </div>

              {/* 받은 신청: 신청자 표시 + 클릭 시 프로필 / 보낸 신청: 게시글 작성자 + 게시글 제목 */}
              {isReceived ? (
                <div onClick={() => setProfileTarget(req.sender)} style={{ cursor: 'pointer' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    {req.sender?.profileImg ? (
                      <img src={req.sender.profileImg} alt="" style={{ width: '36px', height: '36px', borderRadius: '50%', objectFit: 'cover', border: '1px solid #e8e8e8', flexShrink: 0 }} />
                    ) : (
                      <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: '#0a0a0a', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                        <span style={{ fontSize: '13px', color: '#fff', fontWeight: '700' }}>{req.sender?.name?.[0]}</span>
                      </div>
                    )}
                    <div>
                      <p style={{ fontSize: '14px', fontWeight: '600', borderBottom: '1px solid #0a0a0a', display: 'inline' }}>
                        {req.sender?.name}
                      </p>
                      <p style={{ fontSize: '12px', color: '#a0a0a0', marginTop: '2px' }}>@{req.sender?.id}</p>
                    </div>
                  </div>
                </div>
              ) : (
                <Link to={`/boards/${req.boardId}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    {req.receiver?.profileImg ? (
                      <img src={req.receiver.profileImg} alt="" style={{ width: '36px', height: '36px', borderRadius: '50%', objectFit: 'cover', border: '1px solid #e8e8e8', flexShrink: 0 }} />
                    ) : (
                      <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: '#0a0a0a', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                        <span style={{ fontSize: '13px', color: '#fff', fontWeight: '700' }}>{req.receiver?.name?.[0]}</span>
                      </div>
                    )}
                    <div>
                      <p style={{ fontSize: '14px', fontWeight: '600', borderBottom: '1px solid #0a0a0a', display: 'inline' }}>{req.receiver?.name}</p>
                      <p style={{ fontSize: '12px', color: '#a0a0a0', marginTop: '2px' }}>
                        @{req.receiver?.id} · {req.boardTitle || `게시글 #${req.boardId}`}
                      </p>
                    </div>
                  </div>
                </Link>
              )}

              <span className={`badge ${badgeMap[req.status]}`}>{statusMap[req.status]}</span>
              <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                {isReceived && req.status === 'PENDING' && (
                  <>
                    <button onClick={() => onAccept(req.requestId)} className="btn btn-dark"
                      style={{ padding: '8px 18px', fontSize: '12px' }}>수락</button>
                    <button onClick={() => onReject(req.requestId)} className="btn btn-outline-dark"
                      style={{ padding: '8px 18px', fontSize: '12px' }}>거절</button>
                  </>
                )}
                {!isReceived && req.status === 'PENDING' && (
                  <button onClick={() => onCancel(req.requestId)} className="btn btn-outline-dark"
                    style={{ padding: '8px 18px', fontSize: '12px', color: '#c8102e', borderColor: '#c8102e' }}>
                    취소
                  </button>
                )}
              </div>
            </div>
            {i < requests.length - 1 && <div className="divider" />}
          </div>
        ))}
      </div>

      {/* 신청자 프로필 모달 */}
      {profileTarget && (
        <div className="modal-overlay" onClick={() => setProfileTarget(null)}>
          <div className="modal" onClick={e => e.stopPropagation()} style={{ maxWidth: '400px' }}>
            <div style={{ textAlign: 'center', marginBottom: '28px' }}>
              {profileTarget.profileImg ? (
                <img src={profileTarget.profileImg} alt="" style={{ width: '120px', height: '120px', borderRadius: '50%', objectFit: 'cover', border: '2px solid #e8e8e8', display: 'block', margin: '0 auto' }} />
              ) : (
                <div style={{ width: '120px', height: '120px', borderRadius: '50%', background: '#0a0a0a', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto' }}>
                  <span style={{ fontSize: '40px', color: '#fff', fontWeight: '700' }}>{profileTarget.name?.[0]}</span>
                </div>
              )}
              <h2 style={{ fontSize: '22px', fontWeight: '800', letterSpacing: '-0.5px', marginTop: '16px', marginBottom: '4px' }}>
                {profileTarget.name}
              </h2>
              <p style={{ fontSize: '13px', color: '#a0a0a0' }}>@{profileTarget.id}</p>
            </div>

            <div style={{ borderTop: '1px solid #e8e8e8' }}>
              {[
                { label: '성별', value: profileTarget.gender === 'MALE' ? '남성' : profileTarget.gender === 'FEMALE' ? '여성' : profileTarget.gender || '—' },
                { label: '나이', value: profileTarget.age ? `${profileTarget.age}세` : '—' },
                { label: '소개', value: profileTarget.introduce || '—' },
              ].map(({ label, value }) => (
                <div key={label} style={{ display: 'grid', gridTemplateColumns: '60px 1fr', padding: '14px 0', borderBottom: '1px solid #f0f0f0' }}>
                  <span style={{ fontSize: '11px', fontWeight: '700', letterSpacing: '1px', textTransform: 'uppercase', color: '#c0c0c0', paddingTop: '1px' }}>{label}</span>
                  <span style={{ fontSize: '14px', color: '#0a0a0a', lineHeight: 1.5 }}>{value}</span>
                </div>
              ))}
            </div>

            <div className="modal-actions" style={{ marginTop: '24px' }}>
              <button onClick={() => setProfileTarget(null)} className="btn btn-outline-dark" style={{ fontSize: '12px' }}>닫기</button>
            </div>
          </div>
        </div>
      )}
    </>
  )
}
