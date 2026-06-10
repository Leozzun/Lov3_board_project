import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getPlaces, searchPlaces, addPlace, updatePlace, deletePlace, uploadImage } from '../api/axios'
import { useAuth } from '../contexts/AuthContext'
import ImageUploader from '../components/ImageUploader'

export default function Places() {
  const [places, setPlaces] = useState([])
  const [keyword, setKeyword] = useState('')
  const [loading, setLoading] = useState(true)
  const [showAddForm, setShowAddForm] = useState(false)
  const [addForm, setAddForm] = useState({ placeName: '', placeImg: '', placeInfo: '' })
  const [editTarget, setEditTarget] = useState(null)
  const [editForm, setEditForm] = useState({ placeName: '', placeImg: '', placeInfo: '' })
  const [toast, setToast] = useState('')
  const { isAdmin } = useAuth()
  const navigate = useNavigate()

  const showToast = (msg) => { setToast(msg); setTimeout(() => setToast(''), 3000) }

  const fetchPlaces = async (kw = '') => {
    setLoading(true)
    try {
      const res = kw ? await searchPlaces(kw) : await getPlaces()
      setPlaces(res.data)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchPlaces() }, [])

  const handleSearch = (e) => { e.preventDefault(); fetchPlaces(keyword) }

  const handleAdd = async (e) => {
    e.preventDefault()
    await addPlace(addForm)
    setShowAddForm(false)
    setAddForm({ placeName: '', placeImg: '', placeInfo: '' })
    fetchPlaces()
    showToast('장소를 등록했습니다')
  }

  const startEdit = (place) => {
    setEditTarget(place.placeId)
    setEditForm({ placeName: place.placeName, placeImg: place.placeImg || '', placeInfo: place.placeInfo || '' })
  }

  const handleUpdate = async (e, placeId) => {
    e.preventDefault()
    try {
      await updatePlace(placeId, editForm)
      setEditTarget(null)
      fetchPlaces()
      showToast('수정했습니다')
    } catch {
      showToast('수정에 실패했습니다')
    }
  }

  const handleDelete = async (placeId, placeName) => {
    if (!window.confirm(`"${placeName}" 장소를 삭제할까요?`)) return
    try {
      await deletePlace(placeId)
      fetchPlaces()
      showToast('삭제했습니다')
    } catch {
      showToast('삭제에 실패했습니다')
    }
  }

  return (
    <div className="page">
      {/* Header */}
      <div style={{ background: '#0a0a0a', padding: '100px 0 80px' }}>
        <div className="container">
          <p style={{ fontSize: '11px', fontWeight: '700', letterSpacing: '4px', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', marginBottom: '20px' }}>
            Destinations
          </p>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', flexWrap: 'wrap', gap: '32px' }}>
            <h1 style={{ fontSize: 'clamp(36px, 5vw, 64px)', fontWeight: '800', color: '#fff', letterSpacing: '-1.5px', lineHeight: 1.05 }}>
              데이트 장소
            </h1>
            <form onSubmit={handleSearch} style={{ display: 'flex', alignItems: 'center', borderBottom: '1.5px solid rgba(255,255,255,0.3)' }}>
              <input type="text" placeholder="장소 검색" value={keyword} onChange={e => setKeyword(e.target.value)}
                style={{ background: 'transparent', border: 'none', padding: '12px 16px 12px 0', fontSize: '15px', color: '#fff', outline: 'none', width: '220px' }} />
              <button type="submit" style={{ background: 'none', border: 'none', color: 'rgba(255,255,255,0.5)', fontSize: '18px', cursor: 'pointer' }}>→</button>
            </form>
          </div>
        </div>
      </div>

      <div className="container" style={{ padding: '80px 40px' }}>
        {isAdmin && (
          <div style={{ marginBottom: '48px', display: 'flex', justifyContent: 'flex-end' }}>
            <button onClick={() => { setShowAddForm(!showAddForm); setAddForm({ placeName: '', placeImg: '', placeInfo: '' }) }}
              className="btn btn-dark" style={{ fontSize: '12px' }}>
              + 장소 등록
            </button>
          </div>
        )}

        {/* 추가 폼 */}
        {showAddForm && (
          <div style={{ borderTop: '2px solid #0a0a0a', borderBottom: '1px solid #e8e8e8', padding: '48px 0', marginBottom: '64px' }}>
            <h3 style={{ fontSize: '18px', fontWeight: '700', marginBottom: '40px' }}>새 장소 등록</h3>
            <form onSubmit={handleAdd} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px 48px' }}>
              <div className="form-group">
                <label>장소명</label>
                <input value={addForm.placeName} onChange={e => setAddForm(p => ({ ...p, placeName: e.target.value }))} required />
              </div>
              <div className="form-group">
                <label>장소 사진</label>
                <ImageUploader
                  currentImg={addForm.placeImg}
                  onUploaded={(url) => setAddForm(p => ({ ...p, placeImg: url }))}
                  uploadFn={uploadImage}
                />
              </div>
              <div className="form-group" style={{ gridColumn: '1 / -1' }}>
                <label>장소 설명</label>
                <textarea value={addForm.placeInfo} onChange={e => setAddForm(p => ({ ...p, placeInfo: e.target.value }))} />
              </div>
              <div style={{ gridColumn: '1 / -1', display: 'flex', gap: '12px' }}>
                <button type="submit" className="btn btn-dark" style={{ fontSize: '12px' }}>등록</button>
                <button type="button" onClick={() => setShowAddForm(false)} className="btn btn-outline-dark" style={{ fontSize: '12px' }}>취소</button>
              </div>
            </form>
          </div>
        )}

        {loading ? (
          <div className="loading">불러오는 중</div>
        ) : places.length === 0 ? (
          <div className="empty"><div className="empty-icon">📍</div><p>검색 결과가 없어요</p></div>
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '2px' }}>
            {places.map((place, i) =>
              editTarget === place.placeId ? (
                <div key={place.placeId} style={{ padding: '28px', background: '#f5f5f5', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                  <p style={{ fontSize: '12px', fontWeight: '700', letterSpacing: '1px', color: '#a0a0a0', textTransform: 'uppercase' }}>장소 수정</p>
                  <form onSubmit={(e) => handleUpdate(e, place.placeId)} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                      <label>장소명</label>
                      <input value={editForm.placeName} onChange={e => setEditForm(p => ({ ...p, placeName: e.target.value }))} required />
                    </div>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                      <label>장소 사진</label>
                      <ImageUploader
                        currentImg={editForm.placeImg}
                        onUploaded={(url) => setEditForm(p => ({ ...p, placeImg: url }))}
                        uploadFn={uploadImage}
                      />
                    </div>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                      <label>설명</label>
                      <textarea value={editForm.placeInfo} onChange={e => setEditForm(p => ({ ...p, placeInfo: e.target.value }))} style={{ minHeight: '80px' }} />
                    </div>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <button type="submit" className="btn btn-dark" style={{ fontSize: '11px', padding: '8px 16px' }}>저장</button>
                      <button type="button" onClick={() => setEditTarget(null)} className="btn btn-outline-dark" style={{ fontSize: '11px', padding: '8px 16px' }}>취소</button>
                    </div>
                  </form>
                </div>
              ) : (
                <div key={place.placeId} style={{ position: 'relative', height: '300px', overflow: 'hidden' }}>
                  <div
                    onClick={() => !isAdmin && navigate(`/boards?placeId=${place.placeId}`)}
                    style={{
                      height: '100%', cursor: isAdmin ? 'default' : 'pointer',
                      background: place.placeImg ? `url(${place.placeImg}) center/cover` : ['#1a1a1a', '#222', '#181818', '#202020'][i % 4],
                    }}
                    onMouseEnter={e => { if (!isAdmin) e.currentTarget.querySelector('.overlay').style.background = 'rgba(0,0,0,0.6)' }}
                    onMouseLeave={e => { if (!isAdmin) e.currentTarget.querySelector('.overlay').style.background = 'linear-gradient(to top,rgba(0,0,0,0.7) 0%,transparent 60%)' }}
                  >
                    <div className="overlay" style={{ position: 'absolute', inset: 0, background: 'linear-gradient(to top,rgba(0,0,0,0.7) 0%,transparent 60%)', transition: 'background 0.3s' }} />
                    <div style={{ position: 'absolute', bottom: 0, left: 0, padding: '28px', color: '#fff' }}>
                      <h3 style={{ fontSize: '18px', fontWeight: '700', letterSpacing: '-0.2px' }}>{place.placeName}</h3>
                      {place.placeInfo && (
                        <p style={{ fontSize: '13px', opacity: 0.6, marginTop: '4px' }}>
                          {place.placeInfo.slice(0, 40)}{place.placeInfo.length > 40 ? '...' : ''}
                        </p>
                      )}
                    </div>
                  </div>
                  {isAdmin && (
                    <div style={{ position: 'absolute', top: '12px', right: '12px', display: 'flex', gap: '6px' }}>
                      <button onClick={() => startEdit(place)} className="btn btn-white"
                        style={{ fontSize: '11px', padding: '6px 12px' }}>수정</button>
                      <button onClick={() => handleDelete(place.placeId, place.placeName)} className="btn btn-white"
                        style={{ fontSize: '11px', padding: '6px 12px', color: '#c8102e' }}>삭제</button>
                    </div>
                  )}
                </div>
              )
            )}
          </div>
        )}
      </div>

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
}

