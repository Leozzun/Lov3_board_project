import { useEffect, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { getBoards, searchBoards, getBoardsByPlace, addBoard, getPlaces } from '../api/axios'
import { useAuth } from '../contexts/AuthContext'

const PAGE_SIZE = 10

export default function Boards() {
  const [boards, setBoards] = useState([])
  const [places, setPlaces] = useState([])
  const [keyword, setKeyword] = useState('')
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ title: '', content: '', placeId: '' })
  const [toast, setToast] = useState('')
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [isFiltered, setIsFiltered] = useState(false)
  const { member } = useAuth()
  const [searchParams] = useSearchParams()
  const placeId = searchParams.get('placeId')

  const showToast = (msg) => { setToast(msg); setTimeout(() => setToast(''), 3000) }

  const fetchBoards = async (page = 0) => {
    setLoading(true)
    setIsFiltered(false)
    try {
      const res = await getBoards(page, PAGE_SIZE)
      setBoards(res.data.content)
      setTotalPages(res.data.totalPages)
      setTotalElements(res.data.totalElements)
      setCurrentPage(res.data.number)
    } finally {
      setLoading(false)
    }
  }

  const fetchByPlace = async () => {
    setLoading(true)
    setIsFiltered(true)
    try {
      const res = await getBoardsByPlace(placeId)
      setBoards(res.data)
      setTotalPages(0)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (placeId) {
      fetchByPlace()
    } else {
      fetchBoards(0)
    }
    getPlaces().then(r => setPlaces(r.data)).catch(() => {})
  }, [placeId])

  const handleSearch = async (e) => {
    e.preventDefault()
    if (!keyword.trim()) { fetchBoards(0); return }
    setLoading(true)
    setIsFiltered(true)
    try {
      const res = await searchBoards(keyword)
      setBoards(res.data)
      setTotalPages(0)
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = async (e) => {
    e.preventDefault()
    await addBoard({ ...form, placeId: Number(form.placeId) })
    setShowForm(false)
    setForm({ title: '', content: '', placeId: '' })
    fetchBoards(0)
    showToast('등록되었습니다')
  }

  const handlePageChange = (page) => {
    fetchBoards(page)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  // 페이지네이션 버튼 범위 계산 (최대 5개)
  const getPageRange = () => {
    const start = Math.max(0, Math.min(currentPage - 2, totalPages - 5))
    const end = Math.min(totalPages, start + 5)
    return Array.from({ length: end - start }, (_, i) => start + i)
  }

  return (
    <div className="page">
      {/* Header */}
      <div style={{ background: '#0a0a0a', padding: '100px 0 80px' }}>
        <div className="container">
          <p style={{ fontSize: '11px', fontWeight: '700', letterSpacing: '4px', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', marginBottom: '20px' }}>
            Community
          </p>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', flexWrap: 'wrap', gap: '32px' }}>
            <h1 style={{ fontSize: 'clamp(36px, 5vw, 64px)', fontWeight: '800', color: '#fff', letterSpacing: '-1.5px', lineHeight: 1.05 }}>
              {placeId ? '장소 게시글' : '데이트 게시판'}
            </h1>
            <form onSubmit={handleSearch} style={{ display: 'flex', alignItems: 'center', borderBottom: '1.5px solid rgba(255,255,255,0.3)' }}>
              <input
                type="text"
                placeholder="게시글 검색"
                value={keyword}
                onChange={e => setKeyword(e.target.value)}
                style={{
                  background: 'transparent', border: 'none',
                  padding: '12px 16px 12px 0',
                  fontSize: '15px', color: '#fff', outline: 'none', width: '220px',
                }}
              />
              <button type="submit" style={{ background: 'none', border: 'none', color: 'rgba(255,255,255,0.5)', fontSize: '18px', cursor: 'pointer' }}>→</button>
            </form>
          </div>
        </div>
      </div>

      <div className="container" style={{ padding: '80px 40px' }}>
        {member && (
          <div style={{ marginBottom: '48px', display: 'flex', justifyContent: 'flex-end' }}>
            <button onClick={() => setShowForm(!showForm)} className="btn btn-dark" style={{ fontSize: '12px' }}>
              + 글 쓰기
            </button>
          </div>
        )}

        {showForm && (
          <div style={{ borderTop: '2px solid #0a0a0a', borderBottom: '1px solid #e8e8e8', padding: '48px 0', marginBottom: '64px' }}>
            <h3 style={{ fontSize: '18px', fontWeight: '700', letterSpacing: '-0.3px', marginBottom: '40px' }}>데이트 신청글 작성</h3>
            <form onSubmit={handleAdd} style={{ display: 'flex', flexDirection: 'column', gap: '32px', maxWidth: '600px' }}>
              <div className="form-group">
                <label>장소 선택</label>
                <select value={form.placeId} onChange={e => setForm(p => ({ ...p, placeId: e.target.value }))} required>
                  <option value="">선택하세요</option>
                  {places.map(p => <option key={p.placeId} value={p.placeId}>{p.placeName}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label>제목</label>
                <input type="text" value={form.title} onChange={e => setForm(p => ({ ...p, title: e.target.value }))} required />
              </div>
              <div className="form-group">
                <label>내용</label>
                <textarea value={form.content} onChange={e => setForm(p => ({ ...p, content: e.target.value }))} required style={{ minHeight: '120px' }} />
              </div>
              <div style={{ display: 'flex', gap: '12px' }}>
                <button type="submit" className="btn btn-dark" style={{ fontSize: '12px' }}>등록</button>
                <button type="button" onClick={() => setShowForm(false)} className="btn btn-outline-dark" style={{ fontSize: '12px' }}>취소</button>
              </div>
            </form>
          </div>
        )}

        {loading ? (
          <div className="loading">불러오는 중</div>
        ) : boards.length === 0 ? (
          <div className="empty"><div className="empty-icon">💌</div><p>게시글이 없어요</p></div>
        ) : (
          <div>
            {/* Table header */}
            <div style={{
              display: 'grid', gridTemplateColumns: '60px 1fr 140px 80px 80px',
              padding: '12px 0', borderBottom: '2px solid #0a0a0a',
              fontSize: '11px', fontWeight: '700', letterSpacing: '1.5px',
              textTransform: 'uppercase', color: '#a0a0a0',
            }}>
              <span>No</span>
              <span>제목</span>
              <span>장소</span>
              <span>작성자</span>
              <span style={{ textAlign: 'right' }}>조회</span>
            </div>

            {boards.map((board, i) => {
              // 페이지네이션 모드: 전체 등록 순 번호 (오래된 글 = 1번)
              const num = isFiltered
                ? boards.length - i
                : totalElements - currentPage * PAGE_SIZE - i
              return (
                <div key={board.boardId}>
                  <Link to={`/boards/${board.boardId}`} style={{
                    display: 'grid',
                    gridTemplateColumns: '60px 1fr 140px 80px 80px',
                    alignItems: 'center',
                    padding: '24px 0',
                    transition: 'opacity 0.2s',
                  }}
                    onMouseEnter={e => e.currentTarget.style.opacity = '0.5'}
                    onMouseLeave={e => e.currentTarget.style.opacity = '1'}
                  >
                    <span style={{ fontSize: '13px', color: '#c0c0c0', fontWeight: '600' }}>
                      {String(num).padStart(2, '0')}
                    </span>
                    <div>
                      <p style={{ fontSize: '16px', fontWeight: '600', letterSpacing: '-0.2px', marginBottom: '4px' }}>
                        {board.title}
                      </p>
                      <p style={{ fontSize: '13px', color: '#a0a0a0' }}>
                        {board.content?.slice(0, 60)}{board.content?.length > 60 ? '...' : ''}
                      </p>
                    </div>
                    <span style={{ fontSize: '12px', color: '#a0a0a0', letterSpacing: '0.3px' }}>
                      {board.place?.placeName || '—'}
                    </span>
                    <span style={{ fontSize: '13px', color: '#5a5a5a' }}>
                      {board.member?.name || '—'}
                    </span>
                    <span style={{ fontSize: '13px', color: '#a0a0a0', textAlign: 'right' }}>
                      {board.viewCount || 0}
                    </span>
                  </Link>
                  <div className="divider" />
                </div>
              )
            })}

            {/* 페이지네이션 */}
            {!isFiltered && totalPages > 1 && (
              <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '4px', marginTop: '64px' }}>
                <button
                  onClick={() => handlePageChange(0)}
                  disabled={currentPage === 0}
                  style={pageBtn(false, currentPage === 0)}
                >
                  «
                </button>
                <button
                  onClick={() => handlePageChange(currentPage - 1)}
                  disabled={currentPage === 0}
                  style={pageBtn(false, currentPage === 0)}
                >
                  ‹
                </button>

                {getPageRange().map(p => (
                  <button
                    key={p}
                    onClick={() => handlePageChange(p)}
                    style={pageBtn(p === currentPage, false)}
                  >
                    {p + 1}
                  </button>
                ))}

                <button
                  onClick={() => handlePageChange(currentPage + 1)}
                  disabled={currentPage === totalPages - 1}
                  style={pageBtn(false, currentPage === totalPages - 1)}
                >
                  ›
                </button>
                <button
                  onClick={() => handlePageChange(totalPages - 1)}
                  disabled={currentPage === totalPages - 1}
                  style={pageBtn(false, currentPage === totalPages - 1)}
                >
                  »
                </button>
              </div>
            )}
          </div>
        )}
      </div>

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
}

function pageBtn(active, disabled) {
  return {
    width: '36px', height: '36px',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    fontSize: '13px', fontWeight: active ? '700' : '500',
    border: active ? '1.5px solid #0a0a0a' : '1.5px solid #e8e8e8',
    background: active ? '#0a0a0a' : '#fff',
    color: active ? '#fff' : disabled ? '#d0d0d0' : '#0a0a0a',
    cursor: disabled ? 'default' : 'pointer',
    transition: 'all 0.15s',
  }
}
