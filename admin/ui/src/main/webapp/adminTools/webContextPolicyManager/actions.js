import { getBins } from '../../reducer'
import { backendError } from '../../actions'

// Bin level
export const replaceAllBins = (bins, whitelistContexts) => ({ type: 'WCPM/REPLACE_ALL_BINS', bins, whitelistContexts })
export const removeBin = () => ({ type: 'WCPM/REMOVE_BIN' })
export const addNewBin = (binNumber) => ({ type: 'WCPM/ADD_BIN', binNumber })
export const editModeOn = (binNumber) => ({ type: 'WCPM/EDIT_MODE_ON', binNumber })
export const editModeCancel = (binNumber) => ({ type: 'WCPM/EDIT_MODE_CANCEL', binNumber })
export const editModeSave = (binNumber) => ({ type: 'WCPM/EDIT_MODE_SAVE', binNumber })
export const confirmRemoveBin = (binNumber) => ({ type: 'WCPM/CONFIRM_REMOVE_BIN', binNumber })
export const cancelRemoveBin = () => ({ type: 'WCPM/CANCEL_REMOVE_BIN' })

// Realm
export const editRealm = (binNumber, value) => ({ type: 'WCPM/EDIT_REALM', binNumber, value })

// Attribute Lists
export const removeAttribute = (attribute) => (binNumber, pathNumber) => ({ type: 'WCPM/REMOVE_ATTRIBUTE_LIST', attribute, binNumber, pathNumber })
export const addAttribute = (attribute) => (binNumber, path) => ({ type: 'WCPM/ADD_ATTRIBUTE_LIST', attribute, binNumber, path })
export const editAttribute = (attribute) => (binNumber, value) => ({ type: 'WCPM/EDIT_ATTRIBUTE_LIST', attribute, binNumber, value })

// Attribute mapping reducers
export const addAttributeMapping = (binNumber) => ({ type: 'WCPM/ADD_ATTRIBUTE_MAPPING', binNumber })
export const removeAttributeMapping = (binNumber, claim) => ({ type: 'WCPM/REMOVE_ATTRIBUTE_MAPPING', binNumber, claim })

// Set Options
export const setPolicyOptions = (options) => ({ type: 'WCPM/SET_OPTIONS', options })

// Errors
export const setError = (component, message) => ({ type: 'WCPM/ERRORS/SET', component, message })
export const clearAllErrors = () => ({ type: 'WCPM/ERRORS/CLEAR' })
export const clearComponentError = (component) => ({ type: 'WCPM/ERRORS/CLEAR_COMPONENT', component })

// Persist Field Validations
export const addContextPath = (attribute, binNumber) => (dispatch, getState) => {
  dispatch(clearComponentError('contextPaths'))

  const bins = getBins(getState())
  const newPath = bins[binNumber].newcontextPaths

  // test for non-empty path
  if (!newPath || newPath.trim() === '') { return }

  // test for duplicate paths
  let duplicateBinNumber
  bins.forEach((bin, binNumber) => bin.contextPaths.forEach((oldPath) => { oldPath === newPath ? duplicateBinNumber = binNumber : null }))

  if (duplicateBinNumber !== undefined) {
    if (duplicateBinNumber === 0) {
      dispatch(setError('contextPaths', 'This path is in the Whitelist.'))
      return
    }
    dispatch(setError('contextPaths', 'This path is already being used in bin #' + duplicateBinNumber + '.'))
    return
  }

  dispatch(addAttribute(attribute)(binNumber))
}

// Fetch
export const updatePolicyBins = (url) => (dispatch, getState) => {
  const opts = {
    method: 'GET',
    credentials: 'same-origin'
  }

  window.fetch(url, opts)
    .then((res) => Promise.all([ res.status, res.json() ]))
    .then(([status, json]) => {
      if (status === 200) {
        dispatch(replaceAllBins(json[0].contextPolicyBins, json[0].whiteListContexts))
        dispatch(fetchOptions('/admin/beta/config/probe/context-policy-manager/options'))
      }
    })
    .catch(() => {
//    TODO handle probe errors
    })
}

const isEmpty = (bin, key) => {
  if (bin[key] && bin[key].trim() !== '') {
    return false
  }
  return true
}

export const persistChanges = (binNumber, url) => (dispatch, getState) => {
  dispatch(clearAllErrors())
  // Check for non-empty edit fields
  const bin = getBins(getState())[getState().toJS().wcpm.editingBinNumber]
  let hasErrors = false
  if (!isEmpty(bin, 'newcontextPaths')) {
    dispatch(setError('contextPaths', 'Field edited but not added. Please add or clear before saving.'))
    hasErrors = true
    console.log('contextpaths error')
  }
  if (!isEmpty(bin, 'newauthenticationTypes')) {
    dispatch(setError('authTypes', 'Field edited but not added. Please add or clear before saving.'))
    hasErrors = true
    console.log('authtypes error')
  }
  if (!isEmpty(bin, 'newrequiredClaim')) {
    dispatch(setError('requiredClaim', 'Field edited but not added. Please add or clear before saving.'))
    hasErrors = true
    console.log('reqClaim error')
  }
  if (!isEmpty(bin, 'newrequiredAttribute')) {
    dispatch(setError('requiredAttribute', 'Field edited but not added. Please add or clear before saving.'))
    hasErrors = true
    console.log('reqAttr error')
  }
  if (hasErrors) {
    dispatch(setError('general', 'Please address marked fields above before saving.'))
    return
  } // do not persist if any errors are present

  // TODO: check for duplicate context paths

  // dispatch(editModeSave(binNumber))

  const formattedBody = {
    configurationType: 'context-policy-manager',
    contextPolicyBins: getBins(getState()).slice(1),
    whiteListContexts: getBins(getState())[0].contextPaths
  }

  const opts = {
    method: 'POST',
    body: JSON.stringify(formattedBody),
    credentials: 'same-origin'
  }

  window.fetch(url, opts)
    .then((res) => Promise.all([ res.status, res.json() ]))
    .then(([status, json]) => {
      // check for server exceptions
      if (json.messages[0].exceptions.length > 0) {
        dispatch(setError('general', 'The server encountered an error. Please check the server logs for more information.'))
        dispatch(backendError(json))
        return
      }
      // handle responses
      const result = json.messages[0].subType
      if (result === 'SUCCESSFUL_PERSIST') {
        dispatch(editModeSave(binNumber))
      } else {
        dispatch(setError('general', 'Could not save. Reason for issue: ' + json.messages[0].message))
      }
    })
    .catch(() => {
//    TODO handle probe errors
      dispatch(setError('general', 'Could not save configuration - there may have been network errors.'))
    })
}

export const fetchOptions = (url) => (dispatch, getState) => {
  const formattedBody = {
    configurationType: 'context-policy-manager',
    contextPolicyBins: getBins(getState()).slice(1),
    whiteListContexts: getBins(getState())[0].contextPaths
  }

  const opts = {
    method: 'POST',
    body: JSON.stringify(formattedBody),
    credentials: 'same-origin'
  }

  window.fetch(url, opts)
    .then((res) => Promise.all([ res.status, res.json() ]))
    .then(([status, json]) => {
      if (status === 200) {
        dispatch(setPolicyOptions(json.probeResults))
      } else {
      }
    })
    .catch(() => {
//    TODO handle probe errors
    })
}

export const confirmRemoveBinAndPersist = (binNumber, url) => (dispatch) => {
  dispatch(confirmRemoveBin(binNumber))
  dispatch(persistChanges(binNumber, url))
}
