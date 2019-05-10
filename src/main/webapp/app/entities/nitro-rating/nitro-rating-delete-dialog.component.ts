import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRating } from 'app/shared/model/rating.model';
import { NitroRatingService } from './nitro-rating.service';

@Component({
    selector: 'jhi-nitro-rating-delete-dialog',
    templateUrl: './nitro-rating-delete-dialog.component.html'
})
export class NitroRatingDeleteDialogComponent {
    rating: IRating;

    constructor(protected ratingService: NitroRatingService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ratingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ratingListModification',
                content: 'Deleted an rating'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-nitro-rating-delete-popup',
    template: ''
})
export class NitroRatingDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rating }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(NitroRatingDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.rating = rating;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/nitro-rating', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/nitro-rating', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
