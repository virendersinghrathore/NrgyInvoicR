import { DataSource, CollectionViewer } from '@angular/cdk/collections';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, finalize, tap } from 'rxjs/operators';
import { Page } from 'src/app/core/model/page.model';
import { PageDefinition } from 'src/app/core/model/page-definition.model';
import { Plan } from 'src/app/plans/model/plan.model';
import { PlanVersion } from 'src/app/plans/model/plan-version.model';
import { PlanVersionsService } from '../plan-versions.service';

export class PlanVersionsListDataSource implements DataSource<PlanVersion> {
  private plansSubject = new BehaviorSubject<PlanVersion[]>([]);
  public plans = this.plansSubject.asObservable();
  public totalElements = 0;
  public loading = false;

  constructor(private planVersionsService: PlanVersionsService) { }

  connect(collectionViewer: CollectionViewer): Observable<PlanVersion[]> {
    return this.plans;
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.plansSubject.complete();
  }

  loadPlanVersions(plan: Plan, pageDefinition: PageDefinition, callback: (page: Page<PlanVersion>) => void = () => { }) {
    this.loading = true;
    this.planVersionsService.findPlanVersions(plan, pageDefinition)
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loading = false),
        tap<Page<PlanVersion>>(page => this.totalElements = page.totalElements),
        tap<Page<PlanVersion>>(page => callback(page))
      )
      .subscribe(page => this.plansSubject.next(page.content));
  }
}