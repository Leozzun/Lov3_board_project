import { useEffect, useState } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { getBoard, updateBoard, deleteBoard, sendRequest, getPlaces } from '../api/axios'
import { useAuth } from '../contexts/AuthContext'

export default function BoardDetail() {
  const { boardId } = useParams()
  const [board, setBoard] = useState(null)
  const [loading, setLoading] = useState(true)
  const [showRequestModal, setShowRequestModal] = useState(false)
  const [showEditModal, setShowEditModal] = useState(false)
  const [showProfileModal, setShowProfileModal] = useState(false)
  const [message, setMessage] = useState('')
  const [sending, setSending] = useState(false)
  const [toast, setToast] = useState('')
  const { member, isAdmin } = useAuth()
  const navigate = useNavigate()

  const fetchBoard = () =>
    getBoard(boardId)
      .then(r => setBoard(r.data))
      .catch(() => navigate('/boards'))
      .finally(() => setLoading(false))

  useEffect(() => { fetchBoard() }, [boardId])

  const showToast = (msg) => { setToast(msg); setTimeout(() => setToast(''), 3000) }

  const handleDelete = async () => {
    if (!window.confirm('게시글을 삭제할까요?')) return
    await deleteBoard(boardId)
    showToast('삭제되었습니다')
    setTimeout(() => navigate('/boards'), 1200)
  }

  const handleSendRequest = async (e) => {
    e.preventDefault()
    if (!member) { navigate('/login'); return }
    setSending(true)
    try {
      await sendRequest(boardId, { message })
      setShowRequestModal(false)
      setMessage('')
      showToast('데이트 신청을 보냈어요')
    } catch (err) {
      showToast(err.response?.data?.message || '신청에 실패했어요')
    } finally {
      setSending(false)
    }
  }

  const isOwner = member && board && member.memberNo === board.member?.memberNo
  const canDelete = isOwner || isAdmin
  const place = board?.place
  const author = board?.member

  if (loading) return <div className="page loading">불러오는 중</div>
  if (!board) return null

  return (
    <div className="page" style={{ background: '#fff' }}>

      {/* 장소 히어로 배너 */}
      {place && (
        <div style={{
          position: 'relative', height: '320px',
          background: place.placeImg ? `url(${place.placeImg}) center/cover no-repeat` : '#0a0a0a',
          overflow: 'hidden',
        }}>
          <div style={{ position: 'absolute', inset: 0, background: 'linear-gradient(to top, rgba(0,0,0,0.85) 0%, rgba(0,0,0,0.3) 60%, transparent 100%)' }} />
          <div style={{ position: 'absolute', bottom: 0, left: 0, right: 0, padding: '40px' }}>
            <div style={{ maxWidth: '800px', margin: '0 auto' }}>
              <p style={{ fontSize: '10px', fontWeight: '700', letterSpacing: '3px', textTransform: 'uppercase', color: 'rgba(255,255,255,0.45)', marginBottom: '10px' }}>
                Destination
              </p>
              <h2 style={{ fontSize: 'clamp(24px, 4vw, 40px)', fontWeight: '800', color: '#fff', letterSpacing: '-0.5px', marginBottom: '8px' }}>
                {place.placeName}
              </h2>
              {place.placeInfo && (
                <p style={{ fontSize: '14px', color: 'rgba(255,255,255,0.6)', lineHeight: 1.6 }}>{place.placeInfo}</p>
              )}
            </div>
          </div>
        </div>
      )}

      <div className="container" style={{ maxWidth: '800px', padding: '64px 40px' }}>
        {/* Breadcrumb */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '12px', color: '#a0a0a0', marginBottom: '48px' }}>
          <Link to="/boards" style={{ color: '#a0a0a0' }}>게시판</Link>
          <span>/</span>
          {place && (
            <>
              <Link to={`/boards?placeId=${place.placeId}`} style={{ color: '#a0a0a0' }}>{place.placeName}</Link>
              <span>/</span>
            </>
          )}
          <span style={{ color: '#0a0a0a' }}>상세</span>
        </div>

        {/* 제목 */}
        <h1 style={{ fontSize: 'clamp(26px, 4vw, 44px)', fontWeight: '800', letterSpacing: '-1px', lineHeight: 1.1, marginBottom: '36px' }}>
          {board.title}
        </h1>

        {/* 메타 */}
        <div style={{ display: 'flex', gap: '32px', flexWrap: 'wrap', paddingBottom: '36px', borderBottom: '1px solid #e8e8e8', marginBottom: '52px' }}>
          {/* 작성자 — 클릭 시 프로필 모달 */}
          <div
            onClick={() => setShowProfileModal(true)}
            style={{ cursor: 'pointer' }}
          >
            <p style={{ fontSize: '10px', fontWeight: '700', letterSpacing: '2px', textTransform: 'uppercase', color: '#c0c0c0', marginBottom: '6px' }}>작성자</p>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              {author?.profileImg ? (
                <img src={author.profileImg} alt="" style={{ width: '28px', height: '28px', borderRadius: '50%', objectFit: 'cover', border: '1px solid #e8e8e8' }} />
              ) : (
                <div style={{ width: '28px', height: '28px', borderRadius: '50%', background: '#0a0a0a', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <span style={{ fontSize: '11px', color: '#fff', fontWeight: '700' }}>{author?.name?.[0]}</span>
                </div>
              )}
              <p style={{ fontSize: '14px', fontWeight: '600', color: '#0a0a0a', borderBottom: '1px solid #0a0a0a' }}>{author?.name}</p>
            </div>
          </div>

          {[
            { label: '조회', value: `${board.viewCount || 0}회` },
            { label: '작성일', value: board.regDate?.slice(0, 10) },
          ].map(({ label, value }) => (
            <div key={label}>
              <p style={{ fontSize: '10px', fontWeight: '700', letterSpacing: '2px', textTransform: 'uppercase', color: '#c0c0c0', marginBottom: '6px' }}>{label}</p>
              <p style={{ fontSize: '14px', fontWeight: '600', color: '#0a0a0a' }}>{value}</p>
            </div>
          ))}
        </div>

        {/* 본문 */}
        <div style={{ fontSize: '16px', lineHeight: 2, color: '#1a1a1a', marginBottom: '80px', whiteSpace: 'pre-wrap' }}>
          {board.content}
        </div>

        {/* 액션 */}
        <div style={{ display: 'flex', gap: '12px', paddingTop: '40px', borderTop: '1px solid #e8e8e8' }}>
          {isOwner ? (
            <>
              <button onClick={() => setShowEditModal(true)} className="btn btn-dark" style={{ fontSize: '12px' }}>수정</button>
              <button onClick={handleDelete} className="btn btn-outline-dark" style={{ fontSize: '12px', color: '#c8102e', borderColor: '#c8102e' }}>삭제</button>
            </>
          ) : canDelete ? (
            <button onClick={handleDelete} className="btn btn-outline-dark" style={{ fontSize: '12px', color: '#c8102e', borderColor: '#c8102e' }}>삭제 (관리자)</button>
          ) : member ? (
            <button onClick={() => setShowRequestModal(true)} className="btn btn-dark" style={{ fontSize: '12px' }}>데이트 신청하기</button>
          ) : (
            <Link to="/login" className="btn btn-dark" style={{ fontSize: '12px' }}>로그인 후 신청하기</Link>
          )}
          <Link to="/boards" className="btn btn-outline-dark" style={{ fontSize: '12px' }}>목록</Link>
        </div>
      </div>

      {/* 작성자 프로필 모달 */}
      {showProfileModal && (
        <div className="modal-overlay" onClick={() => setShowProfileModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()} style={{ maxWidth: '400px' }}>
            {/* 프로필 사진 */}
            <div style={{ textAlign: 'center', marginBottom: '28px' }}>
              {author?.profileImg ? (
                <img src={author.profileImg} alt="" style={{ width: '120px', height: '120px', borderRadius: '50%', objectFit: 'cover', border: '2px solid #e8e8e8', display: 'block', margin: '0 auto' }} />
              ) : (
                <div style={{ width: '120px', height: '120px', borderRadius: '50%', background: '#0a0a0a', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto' }}>
                  <span style={{ fontSize: '40px', color: '#fff', fontWeight: '700' }}>{author?.name?.[0]}</span>
                </div>
              )}
              <h2 style={{ fontSize: '22px', fontWeight: '800', letterSpacing: '-0.5px', marginTop: '16px', marginBottom: '4px' }}>
                {author?.name}
              </h2>
              <p style={{ fontSize: '13px', color: '#a0a0a0' }}>@{author?.id}</p>
            </div>

            {/* 프로필 정보 */}
            <div style={{ borderTop: '1px solid #e8e8e8' }}>
              {[
                { label: '성별', value: author?.gender === 'MALE' ? '남성' : author?.gender === 'FEMALE' ? '여성' : author?.gender || '—' },
                { label: '나이', value: author?.age ? `${author.age}세` : '—' },
                { label: '소개', value: author?.introduce || '—' },
              ].map(({ label, value }) => (
                <div key={label} style={{ display: 'grid', gridTemplateColumns: '60px 1fr', padding: '14px 0', borderBottom: '1px solid #f0f0f0' }}>
                  <span style={{ fontSize: '11px', fontWeight: '700', letterSpacing: '1px', textTransform: 'uppercase', color: '#c0c0c0', paddingTop: '1px' }}>{label}</span>
                  <span style={{ fontSize: '14px', color: '#0a0a0a', lineHeight: 1.5 }}>{value}</span>
                </div>
              ))}
            </div>

            <div className="modal-actions" style={{ marginTop: '24px' }}>
              <button onClick={() => setShowProfileModal(false)} className="btn btn-outline-dark" style={{ fontSize: '12px' }}>닫기</button>
              {member && !isOwner && (
                <button onClick={() => { setShowProfileModal(false); setShowRequestModal(true) }} className="btn btn-dark" style={{ fontSize: '12px' }}>
                  데이트 신청하기
                </button>
              )}
            </div>
          </div>
        </div>
      )}

      {/* 데이트 신청 모달 */}
      {showRequestModal && (
        <div className="modal-overlay" onClick={() => setShowRequestModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <p style={{ fontSize: '11px', letterSpacing: '2px', color: '#a0a0a0', textTransform: 'uppercase', marginBottom: '16px' }}>Date Request</p>
            <h2 className="modal-title">{author?.name}님께 신청</h2>
            <p style={{ fontSize: '14px', color: '#5a5a5a', marginBottom: '32px', lineHeight: 1.7 }}>간단한 소개와 함께 데이트를 신청해보세요</p>
            <form onSubmit={handleSendRequest}>
              <div className="form-group">
                <label>메시지</label>
                <textarea value={message} onChange={e => setMessage(e.target.value)}
                  placeholder="안녕하세요! 같이 가보고 싶어요 :)" required style={{ minHeight: '120px' }} />
              </div>
              <div className="modal-actions">
                <button type="button" onClick={() => setShowRequestModal(false)} className="btn btn-outline-dark" style={{ fontSize: '12px' }}>취소</button>
                <button type="submit" disabled={sending} className="btn btn-dark" style={{ fontSize: '12px' }}>
                  {sending ? '전송 중...' : '신청 보내기'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* 게시글 수정 모달 */}
      {showEditModal && (
        <EditModal
          board={board}
          onClose={() => setShowEditModal(false)}
          onSaved={(updated) => { setBoard(updated); setShowEditModal(false); showToast('수정했습니다') }}
        />
      )}

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
}

function EditModal({ board, onClose, onSaved }) {
  const [places, setPlaces] = useState([])
  const [form, setForm] = useState({ title: board.title, content: board.content, placeId: board.place?.placeId || '' })
  const [saving, setSaving] = useState(false)

  useEffect(() => { getPlaces().then(r => setPlaces(r.data)).catch(() => {}) }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      const res = await updateBoard(board.boardId, { ...form, placeId: Number(form.placeId) })
      onSaved(res.data)
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()} style={{ maxWidth: '560px' }}>
        <p style={{ fontSize: '11px', letterSpacing: '2px', color: '#a0a0a0', textTransform: 'uppercase', marginBottom: '16px' }}>Edit Post</p>
        <h2 className="modal-title" style={{ marginBottom: '32px' }}>게시글 수정</h2>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          <div className="form-group">
            <label>장소</label>
            <select value={form.placeId} onChange={e => setForm(p => ({ ...p, placeId: e.target.value }))} required>
              <option value="">선택하세요</option>
              {places.map(pl => <option key={pl.placeId} value={pl.placeId}>{pl.placeName}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>제목</label>
            <input value={form.title} onChange={e => setForm(p => ({ ...p, title: e.target.value }))} required />
          </div>
          <div className="form-group">
            <label>내용</label>
            <textarea value={form.content} onChange={e => setForm(p => ({ ...p, content: e.target.value }))} required style={{ minHeight: '140px' }} />
          </div>
          <div className="modal-actions">
            <button type="button" onClick={onClose} className="btn btn-outline-dark" style={{ fontSize: '12px' }}>취소</button>
            <button type="submit" disabled={saving} className="btn btn-dark" style={{ fontSize: '12px' }}>
              {saving ? '저장 중...' : '저장'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
