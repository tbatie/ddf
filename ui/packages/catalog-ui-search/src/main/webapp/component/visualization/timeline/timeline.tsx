// TODO - probably don't want to use this
var metacardDefinitions = require('../../singletons/metacard-definitions.js')
import Timeline from '@connexta/atlas/atoms/timeline'
import * as React from 'react'
import { hot } from 'react-hot-loader'
import EnumDropdown from '../../../react-component/container/input-wrappers/enum'
import withListenTo, {
  WithBackboneProps,
} from '../../../react-component/container/backbone-container'
const properties = require('../../../js/properties')

type Props = WithBackboneProps & {
  selectionInterface: any
}

type Option = {
  label: string
  value: any
}

type Point = {
  icon?: any
  date: Date
  data?: any
}

type State = {
  points: Point[]
  selectedAttr: Option
  attrSelections: Option[]
}

const randomDate = (start: Date, end: Date) =>
  new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()))

export const createSampleData = (sampleSize: number) => {
  let samples = []
  for (let i = 0; i < sampleSize; i++) {
    const date = randomDate(new Date(1990, 0, 1), new Date())
    samples.push({
      date,
    })
  }
  return samples
}

class TimelineVisualization extends React.Component<Props, State> {
  constructor(props: any) {
    super(props)

    this.props.listenTo(
      this.props.selectionInterface.getActiveSearchResults(),
      'update add remove reset',
      this.onSearchResultsChange
    )

    this.state = this.getNewState()
  }

  onAttributeChange = (v: any) => {
    this.setState(this.getNewState(v))
  }

  onSearchResultsChange = () => {
    this.setState(this.getNewState())
  }

  getNewState = (newAttrSelection?: string) => {
    var attrSelections = this.getAvailableDateAtrrs()
    var selectedAttr = this.getSelectedAttr(attrSelections, newAttrSelection)
    var points = this.getTimelinePoints(selectedAttr)

    // console.log('================ NEW STATE ================')
    // console.log('points: ' + JSON.stringify(points.map((v: any) => v.date)))
    // console.log('attr options: ' + JSON.stringify(attrSelections))
    // console.log('selected attr: ' + JSON.stringify(selectedAttr))
    return {
      points,
      selectedAttr,
      attrSelections,
    }
  }

  // TODO - should change scope on this, only should be called inside of getNewState since it requires the points to be updated with this filter as well
  getSelectedAttr = (attrSelections: any, newAttrSelection?: string) => {
    var selectedAttr: any
    if (attrSelections.length === 0) {
      return undefined
    }

    if (newAttrSelection !== undefined) {
      selectedAttr = {
        label: newAttrSelection,
        value: newAttrSelection,
      }
    } else if (
      this.state === undefined ||
      this.state.selectedAttr === undefined ||
      !attrSelections.includes(this.state.selectedAttr)
    ) {
      selectedAttr = attrSelections[0]
    }

    return selectedAttr
  }

  getAvailableDateAtrrs = () => {
    var selectionInterface = this.props.selectionInterface
    var availableAttrs = selectionInterface
      .getActiveSearchResultsAttributes()
      .reduce((list: any, attri: any) => {
        if (metacardDefinitions.metacardTypes[attri].type == 'DATE') {
          list.push({
            label: attri as string,
            value: attri as any,
          })
        }
        return list
      }, [])
    return availableAttrs
  }

  getTimelinePoints = (selectedAttr: any) => {
    var selectionInterface = this.props.selectionInterface
    if (selectedAttr === undefined) {
      return []
    }

    return selectionInterface
      .getActiveSearchResults()
      .reduce((list: any, r: any) => {
        var attrValue = r
          .get('metacard')
          .get('properties')
          .toJSON()[selectedAttr.value]

        if (attrValue !== undefined) {
          list.push({
            date: new Date(attrValue),
            data: r,
          })
        }

        return list
      }, [])
  }

  pointToDisplayInfo = (point: Point) => {
    var displayInfo = properties.summaryShow.reduce((obj: any, attri: any) => {
      var attriVal = point.data
        .get('metacard')
        .get('properties')
        .toJSON()[attri]
      obj[attri] = attriVal
      return obj
    }, {})

    if (displayInfo['title'] === undefined) {
      var title =
        point.data
          .get('metacard')
          .get('properties')
          .toJSON()['title'] || '[NO TITLE]'
      displayInfo['title'] = title
    }

    return displayInfo
  }

  onHover = (point: Point) => {
    return this.pointToDisplayInfo(point)
  }

  onHoverCluster = (points: Point[]) => {
    var pointsToPreview = points.slice(0, 5)
    var dataToDisplay: any[] = pointsToPreview.map(point =>
      this.pointToDisplayInfo(point)
    )
    if (points.length > 5) {
      dataToDisplay.push({ 'hidden metacard': points.length - 5 })
    }

    return dataToDisplay
  }

  onClick = (point: Point) => {
    this.props.selectionInterface.clearSelectedResults()
    this.props.selectionInterface.addSelectedResult(point.data)
  }

  onClickCluster = (points: Point[]) => {
    this.props.selectionInterface.clearSelectedResults()
    this.props.selectionInterface.addSelectedResult(points.map(p => p.data))
  }

  render() {
    // console.log('=================== RENDER STATE ===================')
    // console.log('points: ' + JSON.stringify(points))
    // console.log('attr options: ' + JSON.stringify(attrSelections))
    // console.log('selected attr: ' + JSON.stringify(selectedAttr))

    // console.log(
    //   'active search results: ' +
    //     JSON.stringify(this.props.selectionInterface.getActiveSearchResults())
    // )
    if (this.props.selectionInterface.getActiveSearchResults().length === 0) {
      return <div>Please select a result set to display on the timeline.</div>
    }

    if (this.state.attrSelections.length === 0) {
      return <div>No date attributes exist on metacards of search results</div>
    }

    return (
      <div>
        <EnumDropdown
          label="Select Date attribute to display on timeline"
          onChange={this.onAttributeChange}
          options={this.state.attrSelections}
          value={this.state.selectedAttr}
        />
        <Timeline
          value={this.state.points}
          onHover={this.onHover}
          onHoverCluster={this.onHoverCluster}
          onClick={this.onClick}
          onClickCluster={this.onClickCluster}
        />
      </div>
    )
  }
}

export default hot(module)(withListenTo(TimelineVisualization))
