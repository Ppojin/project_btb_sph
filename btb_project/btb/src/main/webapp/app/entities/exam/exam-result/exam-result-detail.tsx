import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './exam-result.reducer';
import { IExamResult } from 'app/shared/model/exam/exam-result.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExamResultDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ExamResultDetail = (props: IExamResultDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { examResultEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="btbApp.examExamResult.detail.title">ExamResult</Translate> [<b>{examResultEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="idExcerciseResult">
              <Translate contentKey="btbApp.examExamResult.idExcerciseResult">Id Excercise Result</Translate>
            </span>
          </dt>
          <dd>{examResultEntity.idExcerciseResult}</dd>
          <dt>
            <span id="score">
              <Translate contentKey="btbApp.examExamResult.score">Score</Translate>
            </span>
          </dt>
          <dd>{examResultEntity.score}</dd>
          <dt>
            <span id="gitUrl">
              <Translate contentKey="btbApp.examExamResult.gitUrl">Git Url</Translate>
            </span>
          </dt>
          <dd>{examResultEntity.gitUrl}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="btbApp.examExamResult.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {examResultEntity.startDate ? <TextFormat value={examResultEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="btbApp.examExamResult.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{examResultEntity.endDate ? <TextFormat value={examResultEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="result">
              <Translate contentKey="btbApp.examExamResult.result">Result</Translate>
            </span>
          </dt>
          <dd>
            {examResultEntity.result ? (
              <div>
                {examResultEntity.resultContentType ? (
                  <a onClick={openFile(examResultEntity.resultContentType, examResultEntity.result)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {examResultEntity.resultContentType}, {byteSize(examResultEntity.result)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/exam-result" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exam-result/${examResultEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ examResult }: IRootState) => ({
  examResultEntity: examResult.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ExamResultDetail);
